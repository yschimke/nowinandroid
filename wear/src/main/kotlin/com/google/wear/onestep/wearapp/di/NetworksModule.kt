/*
 * Copyright 2021 Google LLC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.wear.onestep.wearapp.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import coil.ImageLoader
import com.google.android.horologist.networks.data.DataRequestRepository
import com.google.android.horologist.networks.data.RequestType
import com.google.android.horologist.networks.db.DBDataRequestRepository
import com.google.android.horologist.networks.db.NetworkUsageDao
import com.google.android.horologist.networks.db.NetworkUsageDatabase
import com.google.android.horologist.networks.highbandwidth.HighBandwidthNetworkMediator
import com.google.android.horologist.networks.highbandwidth.StandardHighBandwidthNetworkMediator
import com.google.android.horologist.networks.logging.NetworkStatusLogger
import com.google.android.horologist.networks.okhttp.AlwaysHttpsInterceptor
import com.google.android.horologist.networks.okhttp.NetworkAwareCallFactory
import com.google.android.horologist.networks.okhttp.NetworkSelectingCallFactory
import com.google.android.horologist.networks.request.NetworkRequester
import com.google.android.horologist.networks.request.NetworkRequesterImpl
import com.google.android.horologist.networks.rules.NetworkingRules
import com.google.android.horologist.networks.rules.NetworkingRulesEngine
import com.google.android.horologist.networks.status.NetworkRepository
import com.google.android.horologist.networks.status.NetworkRepositoryImpl
import com.google.wear.onestep.BuildConfig
import com.google.wear.onestep.auth.AuthRepository
import com.google.wear.onestep.auth.ServerClientId
import com.google.wear.onestep.auth.TokenAccess
import com.google.wear.onestep.navigation.IntentBuilder
import com.google.wear.onestep.navigation.NavDeepLinkIntentBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
object NetworksModule {
    @Singleton
    @Provides
    fun connectivityManager(@ApplicationContext applicationContext: Context): ConnectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Singleton
    @Provides
    fun wifiManager(@ApplicationContext applicationContext: Context): WifiManager =
        applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Singleton
    @Provides
    fun logger(): NetworkStatusLogger = NetworkStatusLogger.InMemory()

    @Singleton
    @Provides
    fun highBandwidthRequester(
        logger: NetworkStatusLogger,
        coroutineScope: CoroutineScope,
        networkRequester: NetworkRequester,
    ): HighBandwidthNetworkMediator =
        StandardHighBandwidthNetworkMediator(logger, networkRequester, coroutineScope, 5.seconds)

    @Singleton
    @Provides
    fun networkRepository(
        connectivityManager: ConnectivityManager,
        coroutineScope: CoroutineScope,
    ): NetworkRepository = NetworkRepositoryImpl(connectivityManager, coroutineScope)

    @Singleton
    @Provides
    fun provideNetworkUsageDatabase(
        @ApplicationContext application: Context,
    ): NetworkUsageDatabase {
        return NetworkUsageDatabase.getDatabase(context = application, multiprocess = false)
    }

    @Singleton
    @Provides
    fun provideNetworkUsageDao(
        networkUsageDatabase: NetworkUsageDatabase,
    ): NetworkUsageDao {
        return networkUsageDatabase.networkUsageDao()
    }

    @Singleton
    @Provides
    fun dataRequestRepository(
        networkUsageDao: NetworkUsageDao,
        coroutineScope: CoroutineScope,
    ): DataRequestRepository = DBDataRequestRepository(networkUsageDao, coroutineScope)

    @Singleton
    @Provides
    fun networkingRules(): NetworkingRules = NetworkingRules.Conservative

    @Singleton
    @Provides
    fun provideDefaultOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .followSslRedirects(false)
            .apply {
                if (!BuildConfig.DEBUG) {
                    addInterceptor(AlwaysHttpsInterceptor)
                }
            }
            .build()

    @Singleton
    @Provides
    fun provideNetworkAwareCallFactory(
        defaultOkHttpClient: OkHttpClient,
        networkingRulesEngine: NetworkingRulesEngine,
        highBandwidthRequester: HighBandwidthNetworkMediator,
        dataRequestRepository: DataRequestRepository,
        networkRepository: NetworkRepository,
        coroutineScope: CoroutineScope
    ): Call.Factory =
        NetworkSelectingCallFactory(
            networkingRulesEngine = networkingRulesEngine,
            dataRequestRepository = dataRequestRepository,
            highBandwidthNetworkMediator = highBandwidthRequester,
            rootClient = defaultOkHttpClient,
            networkRepository = networkRepository,
            coroutineScope = coroutineScope
        )

    @Singleton
    @Provides
    fun networkLogic(
        networkRepository: NetworkRepository,
        appEventLogger: NetworkStatusLogger,
        networkingRules: NetworkingRules,
    ) = NetworkingRulesEngine(
        networkRepository = networkRepository,
        logger = appEventLogger,
        networkingRules = networkingRules
    )

    @Singleton
    @Provides
    fun imageLoader(
        @ApplicationContext application: Context,
        callFactory: Call.Factory
    ): ImageLoader = ImageLoader.Builder(application)
        .crossfade(false)
        .respectCacheHeaders(false)
        .callFactory {
            NetworkAwareCallFactory(
                callFactory,
                defaultRequestType = RequestType.ImageRequest
            )
        }
        .build()

    @Singleton
    @Provides
    fun networkRequester(
        connectivityManager: ConnectivityManager
    ): NetworkRequester = NetworkRequesterImpl(
        connectivityManager
    )
}

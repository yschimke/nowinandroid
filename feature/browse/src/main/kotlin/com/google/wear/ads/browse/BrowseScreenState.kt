package com.google.wear.ads.browse

import com.google.wear.ads.proto.Api.ActivityStatus

data class BrowseScreenState(val activity: ActivityStatus = ActivityStatus.getDefaultInstance())

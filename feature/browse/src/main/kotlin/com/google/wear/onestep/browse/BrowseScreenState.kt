package com.google.wear.onestep.browse

import com.google.wear.onestep.proto.Api.ActivityStatus

data class BrowseScreenState(val activity: ActivityStatus = ActivityStatus.getDefaultInstance())

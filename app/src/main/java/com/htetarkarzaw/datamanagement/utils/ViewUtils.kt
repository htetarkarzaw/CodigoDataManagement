package com.htetarkarzaw.datamanagement.utils

import android.view.LayoutInflater
import android.view.ViewGroup

typealias InflateFragment<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
typealias InflateActivity<T> = (LayoutInflater) -> T
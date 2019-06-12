package com.example.shoutbox.util

import java.io.IOException

class NoConnectivityException : IOException()

class EmptyContentException : Exception()

class WrongLoginException : Exception()
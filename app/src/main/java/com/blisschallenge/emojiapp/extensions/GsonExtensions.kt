package com.blisschallenge.emojiapp.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Optionally, with kotlin you can create a object that inherits
 * from a custom TypeToken for nested generic types (e.g like a List<POJO>):
 *
 *```kotlin
 * val someType = object : TypeToken<List<Repo>>() {}.type
 * ```
 *
 * ## Usage
 *
 * ```kotlin
 * val listPojoType = Gson.genericType<List<Pojo>>()
 * ```
 *
 * See [How to use TypeToken + generics with Gson in Kotlin](https://stackoverflow.com/questions/33381384/how-to-use-typetoken-generics-with-gson-in-kotlin)
 */
inline fun <reified T> Gson.genericType() = object: TypeToken<T>() {}.type
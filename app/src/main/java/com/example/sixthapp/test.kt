package com.example.sixthapp

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

enum class Product(val description: String, val deliveryTime: Long){
    DOOR("doors", 750),
    WINDOWS("windows", 1250)
}

fun order(item:Product) : Product {
    println("order: The ${item.description} are on the way")
    Thread.sleep(item.deliveryTime)
    println("order: The ${item.description} have arrived")
    return item
}

fun perform(taskName: String){
    println("task: Start $taskName")
    Thread.sleep(1000)
    println("task: Finish $taskName")
}

fun main() {


}
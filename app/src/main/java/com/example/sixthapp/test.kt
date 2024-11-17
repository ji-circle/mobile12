package com.example.sixthapp

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

enum class Product(val description: String, val deliveryTime: Long) {
    DOORS("doors", 750),
    WINDOWS("windows", 1250)
}

fun order(item: Product): Product {
    println("order: The ${item.description} are on the way")
    Thread.sleep(item.deliveryTime)
    println("order: The ${item.description} have arrived")
    return item
}

fun perform(taskName: String) {
    println("task: Start $taskName")
    Thread.sleep(1000)
    println("task: Finish $taskName")
}

fun main() {
    /*
        //Product.WINDOWS = 프로덕트 타입.
        // 이걸 order로 넘기고 결과를 windows로 받음.
        val windows = order(Product.WINDOWS)
        val doors = order(Product.DOORS)
        //위에 두 줄에 해당하는 것들이 다 끝날때까지 기다린 뒤 laying bricks를 하게 됨
        perform("laying bricks")
        //order = on the way 1.25초 지난 뒤에 arrived가 되고,
        // perform =  위에가 다 실행된 뒤에 task가 실행됨... 그때는 1초 뒤에 finish가 됨
        perform("installing ${windows.description}")
        perform("installing ${doors.description}")
        //위 코드대로면, doors가 더 빨리 끝남에도 불구하고 위에 줄이 다 끝난 뒤에야 해당 줄이 실행됨
        println(windows)
    */

    //여기부터 위 코드들 수정한것
    runBlocking {
        //어떤 값이 리턴값으로 들어가게 될때에는 launch가 아니라, async 라는 것을 사용
        //  어떤 명령어가 수행되는것만이 아니라 그 수행 이후에 결과값을 받아오는 경우에는 async 사용
        //그리고 이 값이 아래에서 사용될 경우, 해당 변수명 뒤에 .await()을 끼워넣기
        val windows = async { order(Product.WINDOWS) }
        val doors = async { order(Product.DOORS) }

        launch {
            perform("laying bricks")
            perform("installing ${windows.await().description}")
            perform("installing ${doors.await().description}")
        }

        println(windows.await())
    }

}
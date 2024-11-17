package com.example.sixthapp

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

enum class Product(val description: String, val deliveryTime: Long) {
    DOORS("doors", 750),
    WINDOWS("windows", 1250)
}

//delay는 스레드 슬립처럼 스레드를 직접 관리하는 게 아니라, 코루틴의 서스펜드 함수.
//  서스펜드함수는 서스펜드 함수 안에서 호출되어야 하니까 order도 suspend로 만들어준다
//   이렇게 delay를 쓰면, 이걸 delay하는 동안 다른 코루틴이 활동할 수 있게 양보...
suspend fun order(item: Product): Product {
    println("order: The ${item.description} are on the way")
    //delay는 import kotlinx.coroutines.delay 이거의 Long
    delay(item.deliveryTime)
    //delay는 이 시간 뒤에 마저 할테니까 그 사이에는 다른 코루틴이 할수 있도록 양보...
//    Thread.sleep(item.deliveryTime)
    println("order: The ${item.description} have arrived")
    return item
}

suspend fun perform(taskName: String) {
    println("task: Start $taskName")
    delay(1000)
//    Thread.sleep(1000)
    println("task: Finish $taskName")
}

fun main() {

    runBlocking {
        val windows = async { order(Product.WINDOWS) }
        val doors = async { order(Product.DOORS) }

        launch {
            perform("laying bricks")
            //await = 해당 것이 오기전까지 기다리겠다. 잠깐 stop 하는 것
            // 이 둘도 독립적인 scope로 하려면
            launch {
                perform("installing ${windows.await().description}")
            }
            launch {
                perform("installing ${doors.await().description}")
            }
        }
        println(windows.await())
    }

}

/* 이전 코드 - Thread.sleep 사용한 경우
order: The windows are on the way
order: The windows have arrived
order: The doors are on the way
order: The doors have arrived
task: Start laying bricks
task: Finish laying bricks
task: Start installing windows
task: Finish installing windows
task: Start installing doors
task: Finish installing doors
WINDOWS
*/

/* 이후 코드 - delay 사용한 경우, launch 2개로 분리 하기 전
order: The windows are on the way
order: The doors are on the way
task: Start laying bricks                      -- 위 3개가 거의 동시에 나온다 (delay때문에)

order: The doors have arrived
task: Finish laying bricks
order: The windows have arrived
WINDOWS
task: Start installing windows
task: Finish installing windows
task: Start installing doors
task: Finish installing doors
*/


/*
order: The windows are on the way - 1 (-)

order: The doors are on the way   - 3 (-1)

task: Start laying bricks         - 5 (-2)

order: The doors have arrived     - 4 (-)

task: Finish laying bricks        - 6 (-1)

order: The windows have arrived   - 2 (+4)

WINDOWS                           - 11 (-4)
task: Start installing windows    - 7 (+1)
task: Finish installing windows   - 8 (+1)
task: Start installing doors      - 9 (+1)
task: Finish installing doors     - 10 (+1)
*/

/* launch 2개로 분리한 경우
order: The windows are on the way
order: The doors are on the way
task: Start laying bricks
order: The doors have arrived  -- 이 뒤 laying bricks
task: Finish laying bricks
task: Start installing doors
order: The windows have arrived   -- 이거 되기 전에 door가 설치되고 있음
WINDOWS
task: Start installing windows
task: Finish installing doors
task: Finish installing windows
*/
package com.example.sixthapp

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

fun main() {

    //여기 추가
    //코루틴스코프
    //메인 스레드를 잠깐 멈추고 이 안에 있는 것들을 수행하겠다
    //runBlocking이라는 하나의 스코프가 있고, 그 안에 첫번째 launch 스코프, 두번째 launch 스코프가 있음
    //launch 2개 써서 2가지 종류의 코루틴 만들어줌
    // yeild() 는 suspend function. 어떤 것을 잠깐 멈추거나 양보... 현재 코루틴을 실행하다가 잠깐 양보..
    //   코루틴이나 다른 suspend function 안에서만 yield를 호출할 수 있음
    // launch 스코프 안에 yield() 하면 job start 뒤 밑의 launch 먼저 수행된 뒤에 나머지가 실행됨

    runBlocking {

        launch {
            //file reading
            println("Job start")
            yield()
            println("File 1 reading")
            yield()
            println("File 2 reading")
        }

        launch {
            //file download
            println("File 1 download")
            //여기에 yield 써주면 다시 위 코루틴 실행 후 나머지가 실행된다
            yield()
            println("File 2 download")
            yield()
        }

    }

}
package com.example.testcomposeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val countDownFlow = flow<Int> {
        val startingValue = 5
        var currentValue = startingValue
        emit(startingValue)
        while (currentValue > 0){
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }

    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<Int>(0)
    val sharedFlow = _sharedFlow.asSharedFlow()

    init {
        collectFlow2()
    }

    fun incrementCounter(){
        _stateFlow.value += 1
    }

    private fun collectFlow(){
        viewModelScope.launch {

            //Operators - count,reduce and fold

            //count : counts the number of values for the condition
//            val count = countDownFlow.filter {
//                time ->
//                time % 2 == 0
//            }.map {
//                time ->
//                time * time
//            }.onEach {
//                time -> println(time)
//            }.count {
//                it % 2 == 0
//            }

            //reduce adds all the values
//            val reduceResult = countDownFlow.
//                reduce { accumulator, value ->
//                    accumulator + value
//                }

            //fold : takes the initial value to the added value
            val reduceResult = countDownFlow.fold(100){
                acc, value -> acc + value
            }
            println("The count is $reduceResult")
        }
    }

    private fun collectFlow2(){
        val flow1 = flow {
            emit(1)
            delay(500L)
            emit(2)
        }

        viewModelScope.launch {
            flow1.flatMapConcat {value ->
                flow {
                    emit(value + 1)
                    delay(500L)
                    emit(value + 2)
                }
            }.collect{value ->
                println("The value is $value")
            }
        }
    }
}
package com.google.firebase.firestore.ktx.serialization

import com.google.firebase.firestore.ktx.serializers.FirestoreSerializersModule
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import java.lang.Exception

class FirestoreMapBetterEncoder() : AbstractEncoder() {

    private var resultMap =  mutableMapOf<String, Any?>()
    private val map = mutableMapOf<String, Any?>()
    private var elementIndex: Int = 0
    private var descriptor: SerialDescriptor? = null
    private var isBeginOfEncoding: Boolean = true
    private var elementName: MutableList<String> = mutableListOf()

    override val serializersModule: SerializersModule = FirestoreSerializersModule

    override fun encodeValue(value: Any) {
        println("going to encode value $value")
        val key: String = descriptor?.getElementName(elementIndex++) as String
        map.put(key, value)
        println(map)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {

        var key :String? = "dummy"
        val currentElementNameList = this.elementName.toMutableList() // deep copy
        val currentResultMap = this.resultMap // shallow copy
        if (!isBeginOfEncoding) {
            key = this.descriptor?.getElementName(elementIndex++) as String
            println("The key for the current strcture is ${key}")
        }
        return FirestoreMapBetterEncoder().apply {
            this.descriptor = descriptor
            this.isBeginOfEncoding = false
            this.resultMap = currentResultMap
            if (key != null){
                this.elementName = currentElementNameList.also{it.add(key)}
            }


        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        println(">".repeat(60))
        println("This is the end of structure")
        println("end structure map:" + map)
        println("I want to save this to a place where + ${this.elementName}")

        val key = this.elementName.removeLastOrNull()?:"NoValueKey"
        val pathList = this.elementName

        println("The path to save is $pathList, and the key is $key")
        var workingMap = resultMap
        for(path in pathList){
            println(">>>>> $path")
            println(">>>>>>>>>> result map is $resultMap")
            try{
                workingMap = workingMap.get(path) as MutableMap<String, Any?>
                println(">>>>>>>>>>>> I got the map $workingMap")
            }catch (e:Exception){
                println(">> putting >>> $path")
                workingMap.put(path, mutableMapOf<String, Any?>())
                workingMap = workingMap.get(path) as MutableMap<String, Any?>
            }
        }

        try{
            val mapMap = workingMap.get(key) as MutableMap<String, Any?>
            map.forEach{entry ->
                mapMap.put(entry.key, entry.value)
            }
        }catch(e: Exception){
            workingMap.put(key, map)
        }

        println("the final result map is ${resultMap}")
    }

    fun serializedResult(): Map<String, Any?> {
        return resultMap["dummy"] as MutableMap<String, Any?>
    }
}

fun <T> encodeToBetterMap(serializer: SerializationStrategy<T>, value: T): Map<String, Any?> {
    val encoder = FirestoreMapBetterEncoder()
    encoder.encodeSerializableValue(serializer, value)
    return encoder.serializedResult()
}

inline fun <reified T> encodeToBetterMap(value: T): Map<String, Any?> =
    encodeToBetterMap(serializer(), value)

@Serializable data class HomeAddress(val strName: String?="foo-bar")

@Serializable data class Student(val name: String, val id: Int, val address: HomeAddress)

@Serializable data class ClassRoom(val num: Int, val student1: Student, val student2: Student, val totalNum: Int)

val classRoom = ClassRoom(1, Student("chris", 52, HomeAddress("Leseter")), Student("mayson", 53, HomeAddress()),56)

fun main() {
    val encodeMap = encodeToBetterMap(classRoom)
    println(encodeMap)
}

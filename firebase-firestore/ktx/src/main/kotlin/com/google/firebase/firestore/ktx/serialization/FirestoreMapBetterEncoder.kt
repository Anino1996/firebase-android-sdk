package com.google.firebase.firestore.ktx.serialization

import com.google.firebase.firestore.ktx.serializers.FirestoreSerializersModule
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

class FirestoreMapBetterEncoder : AbstractEncoder() {

    private var finalResultMap = mutableMapOf<String, Any?>() // singleton final result map
    private val currentLevelMap = mutableMapOf<String, Any?>() // map of the current structure
    private var elementIndex: Int = 0
    private var descriptor: SerialDescriptor? = null
    private var isBeginOfEncoding: Boolean = true
    private var elementNamePathList: MutableList<String> = mutableListOf()

    override val serializersModule: SerializersModule = FirestoreSerializersModule

    override fun encodeValue(value: Any) {
        val key: String = descriptor?.getElementName(elementIndex++) as String
        currentLevelMap.put(key, value)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {

        var key: String = "dummy"
        val currentNamePath = this.elementNamePathList.toMutableList() // deep copy
        if (!isBeginOfEncoding) {
            key = this.descriptor?.getElementName(elementIndex++) as String
        }
        return FirestoreMapBetterEncoder().also {
            it.finalResultMap = this.finalResultMap // shallow copy to keep it as singleton
            it.descriptor = descriptor
            it.isBeginOfEncoding = false
            it.elementNamePathList = currentNamePath.also { it.add(key) }
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        val key = this.elementNamePathList.removeLastOrNull()!!
        val pathList = this.elementNamePathList
        var currentWorkingMap = finalResultMap
        // build the final result map along the path
        for (path in pathList) {
            try {
                currentWorkingMap = currentWorkingMap.get(path) as MutableMap<String, Any?>
            } catch (e: Exception) {
                // if the path does not exist, create it along the path
                currentWorkingMap.put(path, mutableMapOf<String, Any?>())
                currentWorkingMap = currentWorkingMap.get(path) as MutableMap<String, Any?>
            }
        }
        // put the endStructure map into the global final map
        try {
            val mapMap = currentWorkingMap.get(key) as MutableMap<String, Any?>
            currentLevelMap.forEach { entry -> mapMap.put(entry.key, entry.value) }
        } catch (e: Exception) {
            currentWorkingMap.put(key, currentLevelMap)
        }
    }

    fun serializedResult(): Map<String, Any?> {
        return finalResultMap["dummy"] as MutableMap<String, Any?>
    }
}

fun <T> encodeToBetterMap(serializer: SerializationStrategy<T>, value: T): Map<String, Any?> {
    val encoder = FirestoreMapBetterEncoder()
    encoder.encodeSerializableValue(serializer, value)
    return encoder.serializedResult()
}

inline fun <reified T> encodeToBetterMap(value: T): Map<String, Any?> =
    encodeToBetterMap(serializer(), value)

@Serializable data class HomeAddress(val strName: String? = "foo-bar")

@Serializable data class Student(val name: String, val id: Int, val address: HomeAddress)

@Serializable
data class ClassRoom(val num: Int, val student1: Student, val student2: Student, val totalNum: Int)

val classRoom =
    ClassRoom(
        1,
        Student("chris", 52, HomeAddress("Lester")),
        Student("mayson", 53, HomeAddress()),
        56
    )

fun main() {
    val encodeMap = encodeToBetterMap(classRoom)
    println(encodeMap)
    // This is the result:
    // {student1={address={strName=Lester}, name=chris, id=52}, student2={address={strName=foo-bar}, name=mayson, id=53}, num=1, totalNum=56}
}

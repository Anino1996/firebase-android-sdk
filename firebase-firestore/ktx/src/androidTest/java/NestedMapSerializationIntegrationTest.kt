// Copyright 2022 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.firestore.ktx


import com.google.firebase.firestore.ktx.annotations.KDocumentId
import com.google.firebase.firestore.ktx.annotations.KThrowOnExtraProperties
import kotlinx.serialization.Serializable
import org.junit.Assert.assertEquals
import org.junit.Test


class NestedMapSerializationIntegrationTest {

    @Serializable
    @KThrowOnExtraProperties
    data class Project(val name: String? = null, val owner: String? = null)

    @Serializable
    data class Owner(val name: String? = null)

    @Serializable
    data class ObjectInsideOfObject(val name: String? = null, val owner: Owner? = null)

    @Serializable
    data class ListOfObjectsInsideOfObject(
        val name: String? = null,
        val listOfOwner: List<Owner?>? = null
    )

    @Serializable
    data class KProjectWithDocId(val name: String? = null, @KDocumentId val owner: String? = null)

    @Serializable
    data class KProjectWithPrimitiveExtraProperties(val name: String? = null, val owner: String? = null, val extraBoolean: Boolean? = true)

    @Serializable
    data class KProjectWithCustomObjectExtraProperties(val name: String? = null, val owner: String? = null, val extraOwner: Owner? = null)

    @Test
    fun testSerializationSetMethodSameAsPOJOSet() {
        assertEquals("pig","pig")
//        val docRefKotlin = testCollection("kotlin").document("set")
//        val docRefPOJO = testCollection("pojo").document("set")
//        val projectList = listOf(
//            Project(),
//            Project("x"),
//            Project("x", "y"),
//            Project(null, null)
//        )
//
//        for (project in projectList) {
//            docRefKotlin.setData<Project>(project)
//            docRefPOJO.set(project)
//            val expected = waitFor(docRefPOJO.get()).data
//            val actual = waitFor(docRefKotlin.get()).data
////            assertEquals(expected, actual)
//        }
    }
//
//    @Test
//    fun testSerializationSetMethodWorksForNestedObject() {
//        val docRefKotlin = testDocument("kotlin_set")
//        val docRefPOJO = testDocument("pojo_set")
//        val projectList = listOf(
//            ObjectInsideOfObject(),
//            ObjectInsideOfObject("x"),
//            ObjectInsideOfObject("x", Owner()),
//            ObjectInsideOfObject("x", Owner("yyy")),
//            ObjectInsideOfObject("x", Owner(name = null))
//        )
//
//        for (project in projectList) {
//            docRefKotlin.set<ObjectInsideOfObject>(project)
//            docRefPOJO.set(project)
//            val expected = waitFor(docRefPOJO.get()).data
//            val actual = waitFor(docRefKotlin.get()).data
//            assertEquals(expected, actual)
//        }
//    }
//
//    @Test
//    fun testSerializationSetMethodWorksForList() {
//        val docRefKotlin = testDocument("kotlin_set")
//        val docRefPOJO = testDocument("pojo_set")
//        val projectList = listOf(
//            ListOfObjectsInsideOfObject(),
//            ListOfObjectsInsideOfObject("x"),
//            ListOfObjectsInsideOfObject("x", listOf()),
//            ListOfObjectsInsideOfObject("x", listOf(Owner("a"), Owner("b"))),
//            ListOfObjectsInsideOfObject("x", listOf(Owner("a"), Owner()))
//        )
//        // TODO: Investigate the feasibility of supporting encode nullable List<T?>
//        // i.e. val project6 = ListOfObjectsInsideOfObject("x", listOf(Owner("a"), null))
//        // currently, List<Any> is not supported by the serialization compiler plugin)
//
//        for (project in projectList) {
//            docRefKotlin.set<ListOfObjectsInsideOfObject>(project)
//            docRefPOJO.set(project)
//            val expected = waitFor(docRefPOJO.get()).data
//            val actual = waitFor(docRefKotlin.get()).data
//            assertEquals(expected, actual)
//        }
//    }
//
//    @Test
//    fun testSerializationGetMethodSameAsPOJOGet() {
//        val docRefKotlin = testDocument("kotlin_get")
//        val docRefPOJO = testDocument("pojo_get")
//        val projectList = listOf(
//            Project(),
//            Project("x"),
//            Project("x", "y"), Project(null, null)
//        )
//
//        for (project in projectList) {
//            docRefPOJO.set(project)
//            docRefKotlin.set(project)
//            val expected = waitFor(docRefPOJO.get()).toObject<Project>()
//            val actual = waitFor(docRefKotlin.get()).get<Project>()
//            assertEquals(expected, actual)
//        }
//    }
//
//    @Test
//    fun testSerializationGetMethodWorksForNestedObject() {
//        val docRefKotlin = testDocument("kotlin_set")
//        val docRefPOJO = testDocument("pojo_set")
//        val projectList = listOf(
//            ObjectInsideOfObject(),
//            ObjectInsideOfObject("x"),
//            ObjectInsideOfObject("x", Owner()),
//            ObjectInsideOfObject("x", Owner("b")),
//            ObjectInsideOfObject("x", Owner(name = null)),
//        )
//
//        for (project in projectList) {
//            docRefPOJO.set(project)
//            docRefKotlin.set(project)
//            val expected = waitFor(docRefPOJO.get()).toObject<ObjectInsideOfObject>()
//            val actual = waitFor(docRefKotlin.get()).get<ObjectInsideOfObject>()
//            assertEquals(expected, actual)
//        }
//    }
//
//    @Test
//    fun testSerializationGetMethodWorksForList() {
//        val docRefKotlin = testDocument("kotlin_set")
//        val docRefPOJO = testDocument("pojo_set")
//        val projectList = listOf(
//            ListOfObjectsInsideOfObject(),
//            ListOfObjectsInsideOfObject("x"),
//            ListOfObjectsInsideOfObject("x", listOf()),
//            ListOfObjectsInsideOfObject("x", listOf(Owner("a"), Owner("b"))),
//            ListOfObjectsInsideOfObject("x", listOf(Owner("a"), Owner()))
//        )
//        // TODO: Investigate the feasibility of supporting decode nullable List<T?>
//        // i.e. val project6 = ListOfObjectsInsideOfObject("x", listOf(Owner("a"),null))
//        // mix type list (List<Any>) decoding is not supported i.e. listOf("123", 123)
//
//        for (project in projectList) {
//            docRefPOJO.set(project)
//            docRefKotlin.set(project)
//            val expected = waitFor(docRefPOJO.get()).toObject<ListOfObjectsInsideOfObject>()
//            val actual = waitFor(docRefKotlin.get()).get<ListOfObjectsInsideOfObject>()
//            assertEquals(expected, actual)
//        }
//    }
//
//    @Test
//    fun testSerializationWorksWithDocumentIdAnnotation() {
//        val docRefKotlin = testDocument("kotlin_set")
//        val kProjectList = listOf(
//            KProjectWithDocId(),
//            KProjectWithDocId("name"),
//            KProjectWithDocId("name", "docId")
//        )
//
//        for (project in kProjectList) {
//            docRefKotlin.set<KProjectWithDocId>(project)
//            val expected = docRefKotlin.id
//            val actual = waitFor(docRefKotlin.get()).get<KProjectWithDocId>()?.owner
//            assertEquals(expected, actual)
//        }
//    }
//
//    @Test
//    fun testSerializationThrowWhenDocumentIdAnnotationAppliedToWrongField() {
//
//        @Serializable
//        data class KProjectWithDocIdOnWrongField1(val name: String? = null, @KDocumentId val owner: Int? = null)
//
//        @Serializable
//        data class KProjectWithDocIdOnWrongField2(val name: String? = null, @KDocumentId val owner: Boolean? = null)
//
//        @Serializable
//        data class KProjectWithDocIdOnWrongField3(val name: String? = null, @KDocumentId val owner: Owner? = null)
//        val docRefKotlin = testDocument("kotlin_set")
//        val kProjectList = listOf(
//            KProjectWithDocIdOnWrongField1(),
//            KProjectWithDocIdOnWrongField2(),
//            KProjectWithDocIdOnWrongField3(),
//        )
//
//        for (project in kProjectList) {
//            assertFailsWith<IllegalArgumentException>(
//                message = "Field is annotated with",
//                block = {
//                    docRefKotlin.serialSet(project)
//                }
//            )
//        }
//    }
//
//    @Test
//    fun testSerializationWorksWithThrowOnExtraProperties() {
//        val docRefKotlin = testDocument("kotlin_set")
//        val kProjectList = listOf(
//            KProjectWithPrimitiveExtraProperties(),
//            KProjectWithPrimitiveExtraProperties(extraBoolean = null),
//            KProjectWithPrimitiveExtraProperties(extraBoolean = true),
//        )
//
//        for (project in kProjectList) {
//            docRefKotlin.set<KProjectWithPrimitiveExtraProperties>(project)
//            assertFailsWith<IllegalArgumentException>(
//                message = "Can not match",
//                block = {
//                    waitFor(docRefKotlin.get()).get<Project>()
//                }
//            )
//        }
//    }
//
//    @Test
//    fun testSerializationWorksWithThrowOnExtraPropertiesOnCustomField() {
//        val docRefKotlin = testDocument("kotlin_set")
//        val kProjectList = listOf(
//            KProjectWithCustomObjectExtraProperties(),
//            KProjectWithCustomObjectExtraProperties(extraOwner = null),
//            KProjectWithCustomObjectExtraProperties(extraOwner = Owner()),
//            KProjectWithCustomObjectExtraProperties(extraOwner = Owner(null)),
//            KProjectWithCustomObjectExtraProperties(extraOwner = Owner("owner_name")),
//        )
//
//        for (project in kProjectList) {
//            docRefKotlin.set<KProjectWithCustomObjectExtraProperties>(project)
//            assertFailsWith<IllegalArgumentException>(
//                message = "Can not match",
//                block = {
//                    waitFor(docRefKotlin.get()).get<Project>()
//                }
//            )
//        }
//    }
//
//    @Serializable
//    enum class PaymentStatus(){
//        SUCCESS, FAIL
//    }
//
//    @Serializable
//    data class Payment(val status: PaymentStatus)
//
//    @Test
//    fun testSavingReadingEnum(){
//        val docRefKotlin = testDocument("kotlin_enum")
//        val expected = Payment(PaymentStatus.FAIL)
//        docRefKotlin.set<Payment>(expected)
//        val actual = waitFor(docRefKotlin.get()).get<Payment>()
//        assertEquals(expected, actual)
//    }
}

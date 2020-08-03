package pers.acp.kotlin.test

import kotlinx.coroutines.*
import org.apache.commons.text.CharacterPredicates
import org.apache.commons.text.RandomStringGenerator
import org.bouncycastle.crypto.digests.MD2Digest
import org.bouncycastle.crypto.digests.MD4Digest
import org.bouncycastle.util.encoders.Hex
import org.springframework.expression.spel.standard.SpelExpressionParser
import pers.acp.core.CommonTools
import pers.acp.core.security.Md5Encrypt
import pers.acp.core.security.SignatureEncrypt
import java.io.File
import java.math.BigDecimal
import java.util.*


/**
 * Create by zhangbin on 2017-12-19 11:28
 */
fun main(args: Array<String>) {
    println("Hello World!")

    val generator: RandomStringGenerator = RandomStringGenerator.Builder()
            .withinRange(33, 126)
            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
            .build()
    val s1 = generator.generate(32)
    val s2 = generator.generate(16)
    println(s1)
    println(s2)

    println(System.currentTimeMillis())
    println(Date().time)

    val cls = Class.forName("com.fasterxml.jackson.module.kotlin.KotlinModule")
    println(cls)
    println(cls.canonicalName)

    val path = "C:\\WorkFile\\IdeaProjects\\pers-acp\\acp-admin-cloud\\logs\\log-server"
    val fileName = "..\\..\\log-server.log"
    val file = File("$path\\$fileName")
    println(file.exists())
    println(file.absolutePath)
    println(file.canonicalPath)
    println(file.length())

//    testSpEL()
//    val now = System.currentTimeMillis()
//    println(testKotlin())
//    println("总耗时：" + (System.currentTimeMillis() - now))

//    val regex = """127\.0\.0\..*"""
//    println(CommonTools.regexPattern(regex,"127.0.0.12"))

    var result = "73.25"
    for (i in 1..100000000) {
//        result = Md5Encrypt.encrypt(result)
        result = SignatureEncrypt.encrypt(result, "MD2")
//        val digest = MD4Digest() //通过BC获得消息摘要MD4对象
//        val digest = MD2Digest() //通过BC获得消息摘要MD2对象
//        digest.update(result.toByteArray(Charsets.UTF_8), 0, result.toByteArray(Charsets.UTF_8).size)
//        val md4Byte = ByteArray(digest.digestSize)
//        digest.doFinal(md4Byte, 0)
//        result = Hex.toHexString(md4Byte)
    }
    println(result)

//    val int = 12
//    println(int)
//    println(int.toString())
//    println(CommonTools.getRandomString(18))
//    println(CommonTools.getRandomString(18))
//    println(CommonTools.getRandomString(18))
//    val int = 1.8206778042E10
//    println(BigDecimal(int).toPlainString())
//    println(int.toString())
}

/**
 * 测试 SpringEL 表达式
 */
fun testSpEL() {
    val path = "C:\\WorkFile\\IdeaProjects\\pers-acp\\acp-admin-cloud\\logs\\log-server"
    val fileName = "..\\..\\log-server.log"
    val file = File("$path\\$fileName")
    val parser = SpelExpressionParser()
    val exp1 = parser.parseExpression("canonicalPath")
    println("Expression1 Value: ${exp1.getValue(file)}")

    val testData = TestData()
    val exp2 = parser.parseExpression("test2=13")
    println("Expression2 Value: ${exp2.getValue(testData)}")
    println(testData)

    val number = 5
    val exp3 = parser.parseExpression("#this>=5 and #this<10?'C':'M1'")
    println("Expression3 Value: ${exp3.getValue(number, String::class.java)}")

    testData.test1 = "T10"
    val exp4 = parser.parseExpression("{'T1','T2','T3','T4'}.contains(test1)")
    println("Expression4 Value: ${exp4.getValue(testData, Boolean::class.java)}")

    val list = mutableListOf("1", "2", "3", "A", "B", "C")
    println(list)
    list.iterator().let {
        while (it.hasNext()) {
            val value = it.next()
            it.remove()
            println("value: $value")
        }
    }
    println(list)
    println(testData.toString())
    val exp5 = parser.parseExpression("toString().contains('test1')")
    println("Expression5 Value: ${exp5.getValue(testData, Boolean::class.java)}")
}

fun testKotlin() = runBlocking {
    withContext(Dispatchers.IO) {
        val result = try {
            val job1 = async {
                delay(3000)
                println(1)
            }
            val job2 = async {
                delay(2000)
                println(2)
            }
//            job1.await()
//            job2.await()
            true
        } catch (e: Exception) {
            null
        }
        println("finished")
        result
    }
}
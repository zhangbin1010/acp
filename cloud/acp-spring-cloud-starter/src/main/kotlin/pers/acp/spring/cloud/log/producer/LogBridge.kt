package pers.acp.spring.cloud.log.producer

import pers.acp.spring.cloud.log.LogInfo

/**
 * @author zhangbin by 11/07/2018 14:34
 * @since JDK 11
 */
interface LogBridge {

    fun send(logInfo: LogInfo)

}

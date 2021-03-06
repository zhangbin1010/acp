package pers.acp.spring.boot.init.task

import io.netty.handler.codec.ByteToMessageDecoder
import pers.acp.core.CommonTools
import pers.acp.spring.boot.conf.TcpServerConfiguration
import pers.acp.spring.boot.daemon.DaemonServiceManager
import pers.acp.spring.boot.init.BaseInitTask
import pers.acp.spring.boot.interfaces.LogAdapter
import pers.acp.spring.boot.socket.tcp.TcpServer
import pers.acp.spring.boot.socket.base.ISocketServerHandle

/**
 * 初始化TCP服务
 */
class InitTcpServer(private val logAdapter: LogAdapter,
                    private val tcpServerConfiguration: TcpServerConfiguration,
                    private val socketServerHandleList: List<ISocketServerHandle>,
                    private val byteToMessageDecoderList: List<ByteToMessageDecoder>) : BaseInitTask() {

    fun startTcpServer() {
        logAdapter.info("start tcp listen service ...")
        if (socketServerHandleList.isNotEmpty()) {
            socketServerHandleList.forEach { socketServerHandle -> addServerHandle(socketServerHandle) }
        }
        if (byteToMessageDecoderList.isNotEmpty()) {
            byteToMessageDecoderList.forEach { byteToMessageDecoder -> addMessageDecoder(byteToMessageDecoder) }
        }
        try {
            val listens = tcpServerConfiguration.listeners
            if (listens.isNotEmpty()) {
                listens.forEach { listen ->
                    run {
                        if (listen.enabled) {
                            val beanName = listen.handleBean
                            if (!CommonTools.isNullStr(beanName)) {
                                val handle = getSocketServerHandle(beanName)
                                if (handle != null) {
                                    val port = listen.port
                                    val tcpServer = TcpServer(logAdapter, port, listen, handle, getMessageDecoder(listen.messageDecoder))
                                    val thread = Thread(tcpServer)
                                    thread.isDaemon = true
                                    thread.start()
                                    DaemonServiceManager.addService(tcpServer)
                                    logAdapter.info("start tcp listen service Success [" + listen.name + "] , port:" + listen.port)
                                } else {
                                    logAdapter.error("tcp handle bean [$beanName] is invalid!")
                                }
                            }
                        } else {
                            logAdapter.info("tcp listen service is disabled [" + listen.name + "]")
                        }
                    }
                }
            } else {
                logAdapter.info("No tcp listen service was found")
            }
        } catch (e: Exception) {
            logAdapter.error(e.message, e)
        } finally {
            logAdapter.info("start tcp listen service finished!")
        }
    }

}

package pers.acp.spring.boot.component

import pers.acp.spring.boot.daemon.DaemonServiceManager
import pers.acp.spring.boot.interfaces.Listener
import pers.acp.spring.boot.interfaces.TimerTaskScheduler
import pers.acp.core.interfaces.IDaemonService
import pers.acp.spring.boot.interfaces.LogAdapter

/**
 * 系统控制器
 *
 * @author zhangbin by 2018-1-20 21:24
 * @since JDK 11
 */
class SystemControl(private val logAdapter: LogAdapter,
                    private val listenerMap: Map<String, Listener>?,
                    private val timerTaskScheduler: TimerTaskScheduler) : IDaemonService {

    /**
     * 系统初始化
     */
    fun initialization() {
        try {
            start()
        } catch (e: Exception) {
            this.logAdapter.error(e.message, e)
        }

        DaemonServiceManager.addService(this)
    }

    /**
     * 系统启动
     *
     * @throws Exception 异常
     */
    @Throws(Exception::class)
    fun start() {
        if (listenerMap != null && listenerMap.isNotEmpty()) {
            this.logAdapter.info("start listener begin ...")
            listenerMap.forEach { (key, listener) ->
                this.logAdapter.info("开始启动监听：" + key + " 【" + listener.javaClass.canonicalName + "】")
                listener.startListener()
            }
            this.logAdapter.info("start listener finished!")
        }
        timerTaskScheduler.controlSchedule(TimerTaskScheduler.START)
    }

    /**
     * 系统停止
     */
    fun stop() {
        try {
            if (listenerMap != null && listenerMap.isNotEmpty()) {
                this.logAdapter.info("stop listener begin ...")
                listenerMap.forEach { (key, listener) ->
                    this.logAdapter.info("开始停止监听：" + key + " 【" + listener.javaClass.canonicalName + "】")
                    listener.stopListener()
                }
                this.logAdapter.info("stop listener finished!")
            }
            timerTaskScheduler.controlSchedule(TimerTaskScheduler.STOP)
        } catch (e: Exception) {
            this.logAdapter.error(e.message, e)
        }

    }

    override fun getServiceName(): String {
        return "系统控制服务"
    }

    override fun stopService() {
        stop()
    }
}

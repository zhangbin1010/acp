package pers.acp.spring.cloud.error

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import pers.acp.spring.boot.enums.ResponseCode
import pers.acp.spring.boot.vo.ErrorVo

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

/**
 * @author zhang by 05/03/2019
 * @since JDK 11
 */
class AuthAccessDeniedHandler(private val objectMapper: ObjectMapper) : AccessDeniedHandler {

    @Throws(IOException::class)
    override fun handle(request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException) {
        val errorVO = ErrorVo(
                code = ResponseCode.AuthError.value,
                error = "权限不足",
                errorDescription = "权限不足"
        )
        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        objectMapper.writeValue(response.outputStream, errorVO)
    }

}

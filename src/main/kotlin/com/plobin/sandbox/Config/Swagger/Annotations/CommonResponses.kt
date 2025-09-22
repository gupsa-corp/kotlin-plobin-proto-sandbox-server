package com.plobin.sandbox.Config.Swagger.Annotations

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

object CommonResponses {

    @get:ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = ResponseMessages.SUCCESS),
            ApiResponse(responseCode = "500", description = ResponseMessages.SERVER_ERROR)
        ]
    )
    val StandardSuccess = Unit

    @get:ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = ResponseMessages.CREATED),
            ApiResponse(responseCode = "400", description = ResponseMessages.BAD_REQUEST),
            ApiResponse(responseCode = "422", description = ResponseMessages.VALIDATION_ERROR),
            ApiResponse(responseCode = "500", description = ResponseMessages.SERVER_ERROR)
        ]
    )
    val CreateSuccess = Unit

    @get:ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = ResponseMessages.UPDATED),
            ApiResponse(responseCode = "400", description = ResponseMessages.BAD_REQUEST),
            ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            ApiResponse(responseCode = "422", description = ResponseMessages.VALIDATION_ERROR),
            ApiResponse(responseCode = "500", description = ResponseMessages.SERVER_ERROR)
        ]
    )
    val UpdateSuccess = Unit

    @get:ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = ResponseMessages.DELETED),
            ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            ApiResponse(responseCode = "500", description = ResponseMessages.SERVER_ERROR)
        ]
    )
    val DeleteSuccess = Unit
}
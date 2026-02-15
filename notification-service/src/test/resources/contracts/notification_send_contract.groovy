import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Send notification - POST /notifications"
    
    request {
        method 'POST'
        url '/notifications'
        headers {
            contentType(applicationJson())
        }
        body(
            userId: 123,
            message: "Hello Notification"
        )
    }

    response {
        status 201
        headers {
            contentType(applicationJson())
        }
        body(
            id: 1,
            userId: 123,
            message: "Hello Notification",
            status: 'SENT'
        )
    }
}

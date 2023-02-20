package contracts.notifications

import org.springframework.cloud.contract.spec.Contract

Contract.make {
  description "should return 500 when request dto have to is 500"
  request {
    method POST()
    url("/notifications")
    headers {
      contentType(applicationJson())
    }
    body(
        to: $(consumer(regex('^((?!500).)*$')), producer("tshen.petproject@gmail.com"))
    )
  }
  response {
    status 200
  }
}

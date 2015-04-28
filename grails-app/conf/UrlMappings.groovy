class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')

        "/api/pods"    (controller: 'pod', action: [POST: "create"], parseRequest: true)
        "/api/pods/$id"(controller: 'pod', action: [PUT: "update", PATCH: "update", GET: "get", DELETE: "delete"], parseRequest: true)
	}
}

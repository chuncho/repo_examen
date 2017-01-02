class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }
        "/"(view:"/fenomeno/index/", controller: "fenomeno", action: "index")
        "/fenomeno/clima/$dia"(controller: "fenomeno", action: "clima")

        /*"/"(view:"/index")*/
        "500"(view:'/error')
	}
}

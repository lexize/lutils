# LHttp
## Functions
**get(String uri, [LReader](./LReader.md) reader, LuaTable headers) -> [LHttpResponse](./LHttpResponse.md)**\
Sends HTTP GET request by specified URI, and returns response data with reader

**put(String uri, Object data, [LProvider](./LProvider.md) provider, [LReader](./LReader.md) reader, LuaTable headers) -> [LHttpResponse](./LHttpResponse.md)**\
Sends HTTP PUT request by specified URI, and returns response data with reader

**method(String uri, String method, Object data, [LProvider](./LProvider.md) provider, [LReader](./LReader.md) reader, LuaTable headers) -> [LHttpResponse](./LHttpResponse.md)**\
Sends HTTP request by specified URI with specified method, and returns response data with reader

**delete(String uri, [LReader](./LReader.md) reader, LuaTable headers) -> [LHttpResponse](./LHttpResponse.md)**\
Sends HTTP DELETE request by specified URI, and returns response data with reader

**post(String uri, Object data, [LProvider](./LProvider.md) provider, [LReader](./LReader.md) reader, LuaTable headers) -> [LHttpResponse](./LHttpResponse.md)**\
Sends HTTP POST request by specified URI, and returns response data with reader

**canSendHTTPRequests() -> boolean**

**getStream(String uri, LuaTable headers) -> [LHttpResponse](./LHttpResponse.md)**\
Sends HTTP GET request by specified URI, and returns response with data stream

**getAsync(String uri, [LReader](./LReader.md) reader, LuaTable headers) -> [LFuture](./LFuture.md)**\
Sends async HTTP GET request by specified URI, and returns response data with reader

**getStreamAsync(String uri, LuaTable headers) -> [LFuture](./LFuture.md)**\
Sends async HTTP GET request by specified URI, and returns response with data stream

**postStream(String uri, Object data, [LProvider](./LProvider.md) provider, LuaTable headers) -> [LHttpResponse](./LHttpResponse.md)**\
Sends HTTP POST request by specified URI, and returns response with data stream

**postAsync(String uri, Object data, [LProvider](./LProvider.md) provider, [LReader](./LReader.md) reader, LuaTable headers) -> [LFuture](./LFuture.md)**\
Sends async HTTP POST request by specified URI, and returns response data with reader

**postStreamAsync(String uri, Object data, [LProvider](./LProvider.md) provider, LuaTable headers) -> [LFuture](./LFuture.md)**\
Sends async HTTP POST request by specified URI, and returns response with data stream

**putStream(String uri, Object data, [LProvider](./LProvider.md) provider, LuaTable headers) -> [LHttpResponse](./LHttpResponse.md)**\
Sends HTTP PUT request by specified URI, and returns response with data stream

**putAsync(String uri, Object data, [LProvider](./LProvider.md) provider, [LReader](./LReader.md) reader, LuaTable headers) -> [LFuture](./LFuture.md)**\
Sends async HTTP PUT request by specified URI, and returns response data with reader

**putStreamAsync(String uri, Object data, [LProvider](./LProvider.md) provider, LuaTable headers) -> [LFuture](./LFuture.md)**\
Sends async HTTP PUT request by specified URI, and returns response with data stream

**deleteStream(String uri, LuaTable headers) -> [LHttpResponse](./LHttpResponse.md)**\
Sends HTTP DELETE request by specified URI, and returns response with data stream

**deleteAsync(String uri, [LReader](./LReader.md) reader, LuaTable headers) -> [LFuture](./LFuture.md)**\
Sends async HTTP DELETE request by specified URI, and returns response data with reader

**deleteStreamAsync(String uri, LuaTable headers) -> [LFuture](./LFuture.md)**\
Sends async HTTP DELETE request by specified URI, and returns response with data stream

**methodStream(String uri, String method, Object data, [LProvider](./LProvider.md) provider, LuaTable headers) -> [LHttpResponse](./LHttpResponse.md)**\
Sends HTTP request by specified URI with specified method, and returns stream with response data

**methodAsync(String uri, String method, Object data, [LProvider](./LProvider.md) provider, [LReader](./LReader.md) reader, LuaTable headers) -> [LFuture](./LFuture.md)**\
Sends async HTTP request by specified URI with specified method, and returns response data with reader

**methodStreamAsync(String uri, String method, Object data, [LProvider](./LProvider.md) provider, LuaTable headers) -> [LFuture](./LFuture.md)**\
Sends async HTTP request by specified URI with specified method, and returns stream with response data
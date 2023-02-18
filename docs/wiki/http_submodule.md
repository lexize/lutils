# HTTP submodule
Submodule made for working with HTTP requests.\
All methods can be seen [here](../api_reference/LHttp.md)

With default permissions you cant use HTTP requests. To make it work, avatar permission group must have HTTP requests turned on.\
![img](assets/http_requests_permission.png)

Every HTTP method has their variants: `[method]`, `[method]Stream`, `[method]Async`, and `[method]StreamAsync`\
`[method]` is standard method execution, that accepts `reader`, and may accept `data` and `provider`\
`[method]Stream` works like `[method]`, but it don't accepts `reader`, and in response will return data stream instead of value got by `reader`\
`[method]Async` and `[method]StreamAsync` works like `[method]` and `[method]Stream`, but they are not waiting for response of request, and it can be got later.\
If `data` argument is nil, request will be sent without body.\
If `headers` argument is nil, request will be sent without headers.\
`get` and `delete` methods always executes without body.\
If there is no function for request method you need, you can use `method(uri, method, ...)`, `methodStream(uri, method, ...)`, `methodAsync(uri, method, ...)`, and `methodStreamAsync(uri, method, ...)`

### Example of working with HTTP submodule
```lua
-- Putting needed submodules to variables for fast access to them
local http = lutils.http;
local readers = lutils.readers;
local providers = lutils.providers;

-- Check, does avatar has permission to send http requests
if (not http:canSendHTTPRequests()) then
    print("Avatar don't have permission for sending HTTP requests"); 
    return;
end

-- Sending GET request, and getting it response
local getResponse = http:get("https://example.com", readers.string);
print(getResponse:getCode()) -- Printing response code
print(getResponse:getData()) -- Printing response data. In this case it will be string, because we used string reader.
printTable(getResponse:getHeaders()) -- Printing headers of response

-- Sending GET request, and getting it response, with data stream in response
local getStreamResponse = http:getStream("https://example.com");
print(getStreamResponse:getCode()) -- Printing response code
print(getStreamResponse:getData()) -- Printing response data. In this case it will print LInputStream type name.
printTable(getStreamResponse:getHeaders()) -- Printing headers of response

-- Sending async GET request, and getting future object for it.
local getAsyncResponse = http:getAsync("https://example.com", readers.string);

local function onTick()
    -- Checking, is async request is done.
    if (getAsyncResponse:isDone()) then
        local response = getAsyncResponse:get(); -- Getting response object
        print(response:getCode()) -- Printing response code
        print(response:getData()) -- Printing response data. In this case it will be string, because we used string reader.
        printTable(response:getHeaders()) -- Printing headers of response
        -- Removing tick handler
        events.TICK:remove("async_check_tick")
    end
end
-- Registering tick for checking is async request is done
events.TICK:register(onTick, "async_check_tick");
```
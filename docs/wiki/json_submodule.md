# Json submodule
Submodule made for serialization and deserialization of json strings.

All methods you can see here: [LJson](../api_reference/LJson.md)

### Example of working with json submodule:
```lua
local json = lutils.json; -- Putting json submodule into variable for fast access.

local serializer = json:newSerializer(); -- Creating new serializer with default settings
local prettySerializer = json:newSerializer(
    {prettyPrinting = true}
); -- Creating new serializer with indentation

-- Test array for serialization test
local testArray = {
    "Hello",
    "World!"
};
print(serializer:serialize(testArray)); -- Output: ["Hello", "World!"]
print(prettySerializer:serialize(testArray)); --[[ 
Output: 
[
    "Hello", 
    "World!"
]
]]

-- Test object for serialization test
local testObject = {
    foo = "Hello",
    bar = "World!"
};
print(serializer:serialize(testObject)); -- Output: {"foo":"Hello","bar":"World!"};
print(prettySerializer:serialize(testObject)); --[[ 
Output: 
{
    "foo": "Hello",
    "bar": "World!"
}
]]

-- Test json string for deserialization test
local testJson = "{'foo': 'Hello', 'bar': 'World!'}";

printTable(serializer:deserialize(testJson)); --[[
Output:
{
    ["foo"] = "Hello",
    ["bar"] = "World!"
}
]]
```
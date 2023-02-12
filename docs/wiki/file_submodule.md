# File submodule
Title says everything. Submodule made for working with files.

To start working with files, at first you have to set folder name. To do this, execute `lutils.file:setFolderName("your_folder_name")`\
All file operations will be allowed only in folder by path `[figura_folder]/data/[your_folder_name]`

### Example of working with file submodule:
We have directory by path `[figura_folder]/data/test_avatar`, that contains one file: `test_file.json`
#### Contents of `test_file.json`:
```json
["Hello","World!"]
```
To start working with files we need to do some preparations:
```lua
-- Assigning submodules to variable for fast access. This is optional.
local file = lutils.file;
local readers = lutils.readers;
local providers = lutils.providers;
local json = lutils.json;

-- Creating new serializer with default settings
local serializer = json:newSerializer();

-- Setting folder name
file:setFolderName("test_avatar");
```
Now we can read file that folder contains. To do this we'll use string reader
```lua
-- Here we reading file as string, with string reader.
local fileString = file:read("test_file.json", readers.string);
-- On output we'll get string.
print(fileString); -- Output: ["Hello","World!"]
```
Also we can use JSON reader, to get json object on output
```lua
-- Creating JSON reader.
local jsonReader = serializer:newReader();

local fileData = file:read("test_file.json", jsonReader);
-- On output we'll get table with two strings.
printTable(fileData) --[[ Output:
{
    [1] = "Hello",
    [2] = "World!"
}
]]
```
We can not only read files, but also write data in them. To do this we can use providers.\
Here is how you can write string into file:
```lua
file:write("test_string_file.txt", "Hello world!", providers.string);
```
On output we'll get file with this text:
```
Hello world!
```
If you want to write json representation of value into file, you can create json provider:
```lua
-- Creating JSON provider
local jsonProvider = serializer:newProvider();
-- Writing value into file as JSON
file:write("test_json_file.json", {"Hello", "World!"}, jsonProvider);
```
This is how `test_json_file.json` will look:
```json
["Hello","World!"]
```
If you want to work with raw bytes, you can open input/output stream.\
Let's try to read bytes of `test_file.json`
```lua
-- Opening input stream for file
local readStream = file:openInputStream("test_file.json");
-- Defining table, where we gonna put all bytes
local byteArr = {};
local i = readStream:read(); -- Reading first byte
while i ~= nil do -- If we cant read anymore, stream will return -1, so reading until i will be -1
    byteArr[#byteArr + 1] = i; -- Appending byte to byte table
    i = readStream:read(); -- Reading next byte 
end
readStream:close(); -- Dont forget to close stream!
```
Now we have table with bytes of file, and can work them.\
To show how to write bytes into file, we'll just copy bytes of previous file into new file:
```lua
-- Opening output stream for file
local writeStream = file:openOutputStream("test_file_copy.json");
-- Iterating over table with bytes
for i, v in ipairs(byteArr) do
    writeStream:write(v);
end
writeStream:close(); -- Closing stream to apply changes
```

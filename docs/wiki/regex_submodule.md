# RegEx submodule
Submodule for working with Regular Expressions.\
All methods you can see here: [LRegex](../api_reference/LRegex.md)

### Example of working with RegEx submodule:
```lua
-- Putting regex submodule into variable for fast access.
local regex = lutils.regex;

local fooString, barString = "foo", "bar"; -- Variable for test

-- Checking is fooString matches "foo"
print(regex:isMatches(fooString, "foo")); -- true
-- Checking is fooString matches "foo"
print(regex:isMatches(barString, "foo")); -- false

local HELLOworldString = "HELLO world!"; -- Variable for test

-- Selecting only UPPER CASE letters from string
local HELLOworldMatches = regex:matches(HELLOworldString, "[A-Z]+");

-- Will work only once, because we have only one group of UPPER CASE letters in str
for _, v in ipairs(HELLOworldMatches) do 
    print(v.content); -- HELLO
end

local fooBarString = "foo bar foo bar"; -- Variable for test;

-- Replacing first "foo" with "bar"
print(regex:replaceFirst(fooBarString, "foo", "bar")); -- bar bar foo bar

-- Replacing all "foo" with "bar"
print(regex:replaceAll(fooBarString, "foo", "bar")); -- bar bar bar bar

-- Function that will replace matched text on "bar" if it is "foo", and "foo" otherwise
local function fooBarReplacement(content, startIndex, endIndex, groups) 
    return content == "foo" and "bar" or "foo";
end

-- Using function as replace result provider
print(regex:replaceAll(fooBarString, "(?:foo)|(?:bar)", fooBarReplacement)); -- bar foo bar foo
```
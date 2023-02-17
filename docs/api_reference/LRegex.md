# LRegex
Submodule for working with regular expressions
## Functions
**matches(String string, String pattern) -> [LRegexMatch[]](./LRegexMatch.md)**\
Returns all pattern matches in specified string

**replaceFirst(String string, String pattern, String content) -> String**\
Replaces first match in string with specified content, and returns result

**replaceFirst(String string, String pattern, LuaFunction function) -> String**\
Replaces first match in string with function return value, and returns result. Function accepts match content, start index, end index, and match groups

**replaceAll(String string, String pattern, String content) -> String**\
Replaces all matches in string with specified content, and returns result

**replaceAll(String string, String pattern, LuaFunction function) -> String**\
Replaces all matches in string with function return value, and returns result. Function accepts match content, start index, end index, and match groups

**isMatches(String string, String pattern) -> boolean**\
Checks, is provided string matches specified pattern
package main

import "fmt"

/*
*
2414. 最长的字母序连续子字符串的长度
已解答
中等
相关标签
相关企业
提示
字母序连续字符串 是由字母表中连续字母组成的字符串。换句话说，字符串 "abcdefghijklmnopqrstuvwxyz" 的任意子字符串都是 字母序连续字符串 。

例如，"abc" 是一个字母序连续字符串，而 "acb" 和 "za" 不是。
给你一个仅由小写英文字母组成的字符串 s ，返回其 最长 的 字母序连续子字符串 的长度。

示例 1：

输入：s = "abacaba"
输出：2
解释：共有 4 个不同的字母序连续子字符串 "a"、"b"、"c" 和 "ab" 。
"ab" 是最长的字母序连续子字符串。
示例 2：

输入：s = "abcde"
输出：5
解释："abcde" 是最长的字母序连续子字符串。

提示：

1 <= s.length <= 105
s 由小写英文字母组成
面试中遇到过这道题?
1/5
是
否
通过次数
21.3K
提交次数
32.3K
通过率
*/
func main() {
	substring := longestContinuousSubstring("abcdefghijklmnopqrstuvwxyz")
	fmt.Println(substring)

}

func longestContinuousSubstring(s string) int {
	if len(s) == 1 {
		return 1
	}

	before := s[0]
	temp := 1
	maxCount := 0

	for i := 0; i < len(s); i++ {
		if i != 0 {
			if s[i]-before == 1 {
				temp++
			} else {
				if temp > maxCount {
					maxCount = temp
				}
				temp = 1
			}
			before = s[i]
		}
	}
	if temp > maxCount {
		maxCount = temp
	}
	return maxCount
}

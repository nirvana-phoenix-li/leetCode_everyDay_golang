package main

/*
*
551. 学生出勤记录 I
已解答
简单
相关标签
相关企业
给你一个字符串 s 表示一个学生的出勤记录，其中的每个字符用来标记当天的出勤情况（缺勤、迟到、到场）。记录中只含下面三种字符：

'A'：Absent，缺勤
'L'：Late，迟到
'P'：Present，到场
如果学生能够 同时 满足下面两个条件，则可以获得出勤奖励：

按 总出勤 计，学生缺勤（'A'）严格 少于两天。
学生 不会 存在 连续 3 天或 连续 3 天以上的迟到（'L'）记录。
如果学生可以获得出勤奖励，返回 true ；否则，返回 false 。

示例 1：

输入：s = "PPALLP"
输出：true
解释：学生缺勤次数少于 2 次，且不存在 3 天或以上的连续迟到记录。
示例 2：

输入：s = "PPALLL"
输出：false
解释：学生最后三天连续迟到，所以不满足出勤奖励的条件。

提示：

1 <= s.length <= 1000
s[i] 为 'A'、'L' 或 'P'
面试中遇到过这道题?
1/5
是
否
通过次数
91.7K
提交次数
159.1K
通过率
57.6%
*/
func main() {

}

func checkRecord(s string) bool {
	tempLate := 0
	totalAbsent := 0

	// 使用range遍历字符串
	for _, ch := range s {
		if ch == 'A' {
			tempLate = 0
			totalAbsent++
			if totalAbsent > 1 {
				return false
			}
		} else if ch == 'L' {
			tempLate++
			if tempLate > 3 {
				return false
			}
		} else {
			tempLate = 0
		}

	}
	return true
}

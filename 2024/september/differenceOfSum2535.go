package main

/*
*
2535. 数组元素和与数字和的绝对差
简单
相关标签
相关企业
提示
给你一个正整数数组 nums 。

元素和 是 nums 中的所有元素相加求和。
数字和 是 nums 中每一个元素的每一数位（重复数位需多次求和）相加求和。
返回 元素和 与 数字和 的绝对差。

注意：两个整数 x 和 y 的绝对差定义为 |x - y| 。

示例 1：

输入：nums = [1,15,6,3]
输出：9
解释：
nums 的元素和是 1 + 15 + 6 + 3 = 25 。
nums 的数字和是 1 + 1 + 5 + 6 + 3 = 16 。
元素和与数字和的绝对差是 |25 - 16| = 9 。
示例 2：

输入：nums = [1,2,3,4]
输出：0
解释：
nums 的元素和是 1 + 2 + 3 + 4 = 10 。
nums 的数字和是 1 + 2 + 3 + 4 = 10 。
元素和与数字和的绝对差是 |10 - 10| = 0 。

提示：

1 <= nums.length <= 2000
1 <= nums[i] <= 2000
面试中遇到过这道题?
1/5
是
否
通过次数
19.7K
提交次数
23.5K
通过率
83.9%
*/
func main() {

}

func differenceOfSum(nums []int) int {
	elementSum := 0
	numsSum := 0
	for _, value := range nums {
		elementSum += value
		for value > 0 {
			numsSum += value % 10
			value /= 10
		}
	}

	if elementSum > numsSum {
		return elementSum - numsSum
	} else {
		return numsSum - elementSum
	}

}

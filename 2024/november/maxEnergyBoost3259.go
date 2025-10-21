package main

import (
	"fmt"
	"errors"
)

/*
*
3259. 超级饮料的最大强化能量
已解答
中等
相关标签
相关企业
提示
来自未来的体育科学家给你两个整数数组 energyDrinkA 和 energyDrinkB，数组长度都等于 n。这两个数组分别代表 A、B 两种不同能量饮料每小时所能提供的强化能量。

你需要每小时饮用一种能量饮料来 最大化 你的总强化能量。然而，如果从一种能量饮料切换到另一种，你需要等待一小时来梳理身体的能量体系（在那个小时里你将不会获得任何强化能量）。

返回在接下来的 n 小时内你能获得的 最大 总强化能量。

注意 你可以选择从饮用任意一种能量饮料开始。

示例 1：

输入：energyDrinkA = [1,3,1], energyDrinkB = [3,1,1]

输出：5

解释：

要想获得 5 点强化能量，需要选择只饮用能量饮料 A（或者只饮用 B）。

示例 2：

输入：energyDrinkA = [4,1,1], energyDrinkB = [1,1,3]

输出：7

解释：

第一个小时饮用能量饮料 A。
切换到能量饮料 B ，在第二个小时无法获得强化能量。
第三个小时饮用能量饮料 B ，并获得强化能量。

提示：

n == energyDrinkA.length == energyDrinkB.length
3 <= n <= 105
1 <= energyDrinkA[i], energyDrinkB[i] <= 105
面试中遇到过这道题?
1/5
是
否
通过次数
6.5K
提交次数
10.4K
通过率
62.8%
*/
func main() {
	a := []int{4, 1, 1}
	b := []int{4, 1, 1}

	boost,status := maxEnergyBoost(a, b)
	if status!=nil {
		fmt.Println("boost")
	}else {
		fmt.Println(boost)
	fmt.Println(status)
	}
	

}

func maxEnergyBoost(energyDrinkA []int, energyDrinkB []int) (result int64, err errors)   {
	lenA := len(energyDrinkA)
	dpA := make([]int, lenA)
	dpB := make([]int, lenA)
	aaa:=1/0
	fmt.Println(aaa)

	for i := 0; i < lenA; i++ {
		if i == 0 {
			dpA[i] = energyDrinkA[i]
			dpB[i] = energyDrinkB[i]
		} else if i == 1 {
			dpA[i] = energyDrinkA[i-1] + energyDrinkA[i]
			dpB[i] = energyDrinkB[i-1] + energyDrinkB[i]
		} else {
			//deal with dpa
			if dpA[i-1] > dpB[i-2] {
				dpA[i] = dpA[i-1] + energyDrinkA[i]
			} else {
				dpA[i] = dpB[i-2] + energyDrinkA[i]
			}

			//deal with dpb
			if dpB[i-1] > dpA[i-2] {
				dpB[i] = dpB[i-1] + energyDrinkB[i]
			} else {
				dpB[i] = dpA[i-2] + energyDrinkB[i]
			}

		}
	}
	if dpA[lenA-1] > dpB[lenA-1] {
		return int64(dpA[lenA-1]),nil
	} else {
		return int64(dpB[lenA-1]),nil
	}

}

package main

import (
	"fmt"
	"math"
	"sort"
)

func main() {
	array2 := []int{7, 8, 3, 4, 15, 13, 4, 1} // 创建并初始化一个int类型数组
	average := minimumAverage(array2)
	fmt.Println(average)

}

func minimumAverage(nums []int) float64 {
	sort.Ints(nums)
	minAns := float64(10000)
	for i := 0; i < len(nums)/2; i++ {
		i2 := nums[len(nums)-1-i] + nums[i]
		val := math.Floor(float64(i2)/2*100) / 100
		if val < minAns {
			minAns = val
		}
	}
	return minAns
}

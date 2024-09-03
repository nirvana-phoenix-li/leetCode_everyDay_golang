package main

import (
	"fmt"
	"sort"
)

func main() {

	arr := []int{3, -1, -5, 2, 5, -9}
	strength := maxStrength(arr)
	fmt.Println(strength)

}

func maxStrength(nums []int) int64 {
	if len(nums) == 1 {
		return int64(nums[0])
	}
	original := int64(1)
	flag := false
	neverNegative := true

	sort.Ints(nums)

	for m := len(nums) - 1; m >= 0; m-- {
		if nums[m] > 0 {
			original *= int64(nums[m])
			flag = true
		} else if nums[m] < 0 {
			if neverNegative {
				neverNegative = false
				if m%2 == 0 {
					continue
				}
			}
			original *= int64(nums[m])
			flag = true
		}
	}
	if flag {
		return original
	} else {
		return 0
	}
}

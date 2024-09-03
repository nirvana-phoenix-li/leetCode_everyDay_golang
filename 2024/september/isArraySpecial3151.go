package main

import "fmt"

func main() {
	go sayHello()

	for i := 0; i < 1000000; i++ {

	}
	//nums := []int{2, 1, 4}
	//special := isArraySpecial(nums)
	//fmt.Println(special)

}

func isArraySpecial(nums []int) bool {
	if len(nums) <= 1 {
		return true
	}
	for i := 0; i < len(nums)-1; i++ {
		temp := nums[i] + nums[i+1]
		if temp%2 != 1 {
			return false
		}
	}
	return true
}

func sayHello() {
	fmt.Println("hello world")

}

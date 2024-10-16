package main

import "fmt"

func main() {
	triangle := maxHeightOfTriangle(2, 1)

	fmt.Println(triangle)

}

func maxHeightOfTriangle(red int, blue int) int {

	//red first
	redVal := getMax(red, blue, true)

	//blue first
	blueVal := getMax(red, blue, false)
	if redVal > blueVal {
		return redVal
	} else {
		return blueVal
	}

}

func getMax(red int, blue int, flag bool) int {
	tempVal := 1
	for red >= 0 && blue >= 0 {
		if flag {
			red -= tempVal
		} else {
			blue -= tempVal
		}
		flag = !flag
		tempVal++
	}

	return tempVal - 2
}

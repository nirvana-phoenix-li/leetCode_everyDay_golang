package main

func main() {

}

func distanceBetweenBusStops(distance []int, start int, destination int) int {
	if start > destination {
		temp := destination
		destination = start
		start = temp
	}

	total := 0
	limit := 0
	for index, value := range distance {
		total += value
		if index >= start && index < destination {
			limit++
		}
	}
	var answer int

	if limit >= total-limit {
		answer = total - limit
	} else {
		answer = limit
	}
	return answer
}

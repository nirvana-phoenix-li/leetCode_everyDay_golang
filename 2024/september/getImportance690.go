package main

import "fmt"

func main() {
	//[[1,5,[2,3]],[2,3,[4]],[3,4,[]],[4,1,[]]] 13
	e1 := new(Employee)
	e1.Id = 1
	e1.Importance = 5
	e1.Subordinates = []int{2, 3}
	e2 := &Employee{
		Id:           2,
		Importance:   3,
		Subordinates: []int{4},
	}

	e3 := &Employee{
		Id:           3,
		Importance:   4,
		Subordinates: []int{},
	}
	e4 := &Employee{
		Id:           4,
		Importance:   1,
		Subordinates: []int{},
	}
	employees := []*Employee{e1, e2, e3, e4}
	importance := getImportance(employees, 1)
	fmt.Println(importance)

}

func getImportance(employees []*Employee, id int) int {

	var empls = make([]Employee, 2001)
	hashSet := make(map[int]bool)
	for _, value := range employees {
		empls[value.Id] = *value
		if len(value.Subordinates) != 0 {
			hashSet[value.Id] = true
		}
	}

	for {
		if len(hashSet) == 0 {
			break
		}
		temp := make(map[int]bool)
		for key, _ := range hashSet {
			employee := empls[key]
			removeList := make(map[int]bool)
			for _, index := range employee.Subordinates {
				if !hashSet[index] {
					removeList[index] = true
					employee.Importance += empls[index].Importance
				}
			}

			var result []int
			for _, item := range employee.Subordinates {
				if _, found := removeList[item]; !found {
					result = append(result, item)
				}
			}
			employee.Subordinates = result
			empls[key] = employee

			if len(result) != 0 {
				temp[employee.Id] = true
			}
		}
		hashSet = temp
	}
	return empls[id].Importance
}

type Employee struct {
	Id           int
	Importance   int
	Subordinates []int
}

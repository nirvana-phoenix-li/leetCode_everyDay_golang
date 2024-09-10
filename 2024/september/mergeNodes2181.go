package main

func main() {

	e3 := &ListNode{
		Val:  0,
		Next: nil,
	}
	e2 := &ListNode{
		Val:  5,
		Next: e3,
	}

	e1 := &ListNode{
		Val:  0,
		Next: e2,
	}
	nodes := mergeNodes(e1)
	println(nodes)

}

func mergeNodes(head *ListNode) *ListNode {
	dummyHead := head
	listNode := dummyHead
	temp := 0
	for head.Next != nil {
		next := head.Next
		if next.Val == 0 {
			node := new(ListNode)
			node.Val = temp
			dummyHead.Next = node
			dummyHead = dummyHead.Next
			temp = 0
		} else {
			temp += next.Val
		}

		head = head.Next
	}
	return listNode.Next
}

type ListNode struct {
	Val  int
	Next *ListNode
}

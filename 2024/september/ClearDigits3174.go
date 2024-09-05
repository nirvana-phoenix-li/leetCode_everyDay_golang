package main

func main() {
	digits := clearDigits("abcss")
	println(digits)

}

func clearDigits(s string) string {
	power := 0
	var result []rune

	for m := len(s) - 1; m >= 0; m-- {

		if s[m] <= 57 && s[m] >= 48 {
			power++
		} else {
			if power > 0 {
				power--
			} else {
				u := s[m]
				result = append([]rune{rune(u)}, result...)
			}

		}

	}

	return string(result)
}

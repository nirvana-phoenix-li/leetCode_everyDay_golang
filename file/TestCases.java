import java.util.*;

public class TestCases {
    
    private Integer attainSameRouteMaxLength(List<String> userBehaviors, List<String> riskPath) {
        int maxLength = 0;

        //如果某个轮次长度超出了风险路径长度的一半，毫无疑问，这个就是最长路径
        int halfSize = riskPath.size() / 2;

        //lastIndex靠右的指针
        int lastIndex = riskPath.size();
        //firstIndex靠左的指针
        int firstIndex = riskPath.size();
        //小表驱动大表，从尾部向前统计最长相同路径
        for (int z = firstIndex; z >= 0; z--) {
            int behaviorIndex = userBehaviors.size() - 1;
            while (behaviorIndex-- >= 0) {
                if (riskPath.get(firstIndex).equals(userBehaviors.get(behaviorIndex))) {
                    firstIndex--;
                }
            }
            maxLength = Math.max(maxLength, lastIndex - firstIndex);

            firstIndex--;
            lastIndex = firstIndex;
            if (maxLength > halfSize) {
                return maxLength;
            }
        }
        return maxLength;
    }

    public static void main(String[] args) {
        TestCases test = new TestCases();
        
        // Test Case 1: 完全匹配
        List<String> userBehaviors1 = Arrays.asList("A", "B", "C", "D");
        List<String> riskPath1 = Arrays.asList("A", "B", "C", "D");
        System.out.println("Test 1 - 完全匹配:");
        System.out.println("User: " + userBehaviors1);
        System.out.println("Risk: " + riskPath1);
        System.out.println("Result: " + test.attainSameRouteMaxLength(userBehaviors1, riskPath1));
        System.out.println();

        // Test Case 2: 部分匹配
        List<String> userBehaviors2 = Arrays.asList("A", "B", "C", "D", "E");
        List<String> riskPath2 = Arrays.asList("B", "C", "D");
        System.out.println("Test 2 - 部分匹配:");
        System.out.println("User: " + userBehaviors2);
        System.out.println("Risk: " + riskPath2);
        System.out.println("Result: " + test.attainSameRouteMaxLength(userBehaviors2, riskPath2));
        System.out.println();

        // Test Case 3: 无匹配
        List<String> userBehaviors3 = Arrays.asList("X", "Y", "Z");
        List<String> riskPath3 = Arrays.asList("A", "B", "C");
        System.out.println("Test 3 - 无匹配:");
        System.out.println("User: " + userBehaviors3);
        System.out.println("Risk: " + riskPath3);
        System.out.println("Result: " + test.attainSameRouteMaxLength(userBehaviors3, riskPath3));
        System.out.println();

        // Test Case 4: 空列表
        List<String> userBehaviors4 = new ArrayList<>();
        List<String> riskPath4 = Arrays.asList("A", "B");
        System.out.println("Test 4 - 空用户行为:");
        System.out.println("User: " + userBehaviors4);
        System.out.println("Risk: " + riskPath4);
        System.out.println("Result: " + test.attainSameRouteMaxLength(userBehaviors4, riskPath4));
        System.out.println();

        // Test Case 5: 空风险路径
        List<String> userBehaviors5 = Arrays.asList("A", "B", "C");
        List<String> riskPath5 = new ArrayList<>();
        System.out.println("Test 5 - 空风险路径:");
        System.out.println("User: " + userBehaviors5);
        System.out.println("Risk: " + riskPath5);
        System.out.println("Result: " + test.attainSameRouteMaxLength(userBehaviors5, riskPath5));
        System.out.println();

        // Test Case 6: 重复元素
        List<String> userBehaviors6 = Arrays.asList("A", "A", "A", "B", "B");
        List<String> riskPath6 = Arrays.asList("A", "A", "B");
        System.out.println("Test 6 - 重复元素:");
        System.out.println("User: " + userBehaviors6);
        System.out.println("Risk: " + riskPath6);
        System.out.println("Result: " + test.attainSameRouteMaxLength(userBehaviors6, riskPath6));
        System.out.println();

        // Test Case 7: 长序列
        List<String> userBehaviors7 = new ArrayList<>();
        List<String> riskPath7 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userBehaviors7.add("step" + i);
            riskPath7.add("step" + i);
        }
        System.out.println("Test 7 - 长序列:");
        System.out.println("User length: " + userBehaviors7.size());
        System.out.println("Risk length: " + riskPath7.size());
        System.out.println("Result: " + test.attainSameRouteMaxLength(userBehaviors7, riskPath7));
        System.out.println();

        // Test Case 8: 反向匹配
        List<String> userBehaviors8 = Arrays.asList("D", "C", "B", "A");
        List<String> riskPath8 = Arrays.asList("A", "B", "C", "D");
        System.out.println("Test 8 - 反向匹配:");
        System.out.println("User: " + userBehaviors8);
        System.out.println("Risk: " + riskPath8);
        System.out.println("Result: " + test.attainSameRouteMaxLength(userBehaviors8, riskPath8));
        System.out.println();

        // Test Case 9: 单个元素
        List<String> userBehaviors9 = Arrays.asList("X");
        List<String> riskPath9 = Arrays.asList("X");
        System.out.println("Test 9 - 单个元素:");
        System.out.println("User: " + userBehaviors9);
        System.out.println("Risk: " + riskPath9);
        System.out.println("Result: " + test.attainSameRouteMaxLength(userBehaviors9, riskPath9));
        System.out.println();

        // Test Case 10: 复杂匹配
        List<String> userBehaviors10 = Arrays.asList("login", "browse", "add_cart", "checkout", "payment");
        List<String> riskPath10 = Arrays.asList("browse", "add_cart", "checkout");
        System.out.println("Test 10 - 复杂匹配:");
        System.out.println("User: " + userBehaviors10);
        System.out.println("Risk: " + riskPath10);
        System.out.println("Result: " + test.attainSameRouteMaxLength(userBehaviors10, riskPath10));
    }
}

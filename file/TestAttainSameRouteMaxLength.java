import java.util.*;

public class TestAttainSameRouteMaxLength {
    
    private Integer attainSameRouteMaxLength(List<String> userBehaviors) {
        int maxLength = 0;

        ArrayList<String> riskPath = new ArrayList<>();
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
        TestAttainSameRouteMaxLength test = new TestAttainSameRouteMaxLength();
        
        // 测试案例1: 基本测试 - 完全匹配
        System.out.println("=== 测试案例1: 完全匹配 ===");
        List<String> userBehaviors1 = Arrays.asList("A", "B", "C", "D", "E");
        // 注意：这里riskPath是空的，所以结果应该是0
        System.out.println("用户行为: " + userBehaviors1);
        System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors1));
        System.out.println("预期: 0 (因为riskPath为空)");
        System.out.println();

        // 测试案例2: 空用户行为
        System.out.println("=== 测试案例2: 空用户行为 ===");
        List<String> userBehaviors2 = new ArrayList<>();
        System.out.println("用户行为: " + userBehaviors2);
        System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors2));
        System.out.println("预期: 0");
        System.out.println();

        // 测试案例3: 单个行为
        System.out.println("=== 测试案例3: 单个行为 ===");
        List<String> userBehaviors3 = Arrays.asList("X");
        System.out.println("用户行为: " + userBehaviors3);
        System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors3));
        System.out.println("预期: 0");
        System.out.println();

        // 测试案例4: 多个相同行为
        System.out.println("=== 测试案例4: 多个相同行为 ===");
        List<String> userBehaviors4 = Arrays.asList("A", "A", "A", "A", "A");
        System.out.println("用户行为: " + userBehaviors4);
        System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors4));
        System.out.println("预期: 0");
        System.out.println();

        // 测试案例5: 复杂行为序列
        System.out.println("=== 测试案例5: 复杂行为序列 ===");
        List<String> userBehaviors5 = Arrays.asList("login", "browse", "add_cart", "checkout", "payment", "logout");
        System.out.println("用户行为: " + userBehaviors5);
        System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors5));
        System.out.println("预期: 0");
        System.out.println();

        // 测试案例6: 包含null值的行为
        System.out.println("=== 测试案例6: 包含null值的行为 ===");
        List<String> userBehaviors6 = Arrays.asList("A", null, "B", "C");
        System.out.println("用户行为: " + userBehaviors6);
        try {
            System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors6));
        } catch (Exception e) {
            System.out.println("异常: " + e.getMessage());
        }
        System.out.println("预期: 可能抛出NullPointerException");
        System.out.println();

        // 测试案例7: 长序列
        System.out.println("=== 测试案例7: 长序列 ===");
        List<String> userBehaviors7 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            userBehaviors7.add("action_" + i);
        }
        System.out.println("用户行为长度: " + userBehaviors7.size());
        System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors7));
        System.out.println("预期: 0");
        System.out.println();

        // 测试案例8: 边界条件 - 索引越界测试
        System.out.println("=== 测试案例8: 边界条件测试 ===");
        List<String> userBehaviors8 = Arrays.asList("test");
        System.out.println("用户行为: " + userBehaviors8);
        try {
            System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors8));
        } catch (Exception e) {
            System.out.println("异常: " + e.getMessage());
        }
        System.out.println("预期: 可能抛出IndexOutOfBoundsException");
    }
}

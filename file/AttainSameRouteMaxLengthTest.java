import java.util.*;

/**
 * 测试 attainSameRouteMaxLength 方法的测试类
 * 注意：原方法中 riskPath 是空的 ArrayList，所以所有测试结果都应该是 0
 */
public class AttainSameRouteMaxLengthTest {
    
    private Integer attainSameRouteMaxLength(List<String> userBehaviors) {
        int maxLength = 0;

        ArrayList<String> riskPath = new ArrayList<>(); // 注意：这里是空的！
        //如果某个轮次长度超出了风险路径长度的一半，毫无疑问，这个就是最长路径
        int halfSize = riskPath.size() / 2; // 0 / 2 = 0

        //lastIndex靠右的指针
        int lastIndex = riskPath.size(); // 0
        //firstIndex靠左的指针
        int firstIndex = riskPath.size(); // 0
        //小表驱动大表，从尾部向前统计最长相同路径
        for (int z = firstIndex; z >= 0; z--) { // z从0开始，循环1次
            int behaviorIndex = userBehaviors.size() - 1;
            while (behaviorIndex-- >= 0) {
                // 这里会抛出 IndexOutOfBoundsException，因为 riskPath 是空的
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
        AttainSameRouteMaxLengthTest test = new AttainSameRouteMaxLengthTest();
        
        System.out.println("=== 测试 attainSameRouteMaxLength 方法 ===");
        System.out.println("注意：原方法中 riskPath 是空的 ArrayList，会导致 IndexOutOfBoundsException");
        System.out.println();

        // 测试案例1: 基本测试
        System.out.println("=== 测试案例1: 基本测试 ===");
        List<String> userBehaviors1 = Arrays.asList("A", "B", "C");
        System.out.println("用户行为: " + userBehaviors1);
        try {
            System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors1));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("异常: IndexOutOfBoundsException - " + e.getMessage());
        }
        System.out.println("预期: IndexOutOfBoundsException (因为 riskPath 为空)");
        System.out.println();

        // 测试案例2: 空用户行为
        System.out.println("=== 测试案例2: 空用户行为 ===");
        List<String> userBehaviors2 = new ArrayList<>();
        System.out.println("用户行为: " + userBehaviors2);
        try {
            System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors2));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("异常: IndexOutOfBoundsException - " + e.getMessage());
        }
        System.out.println("预期: IndexOutOfBoundsException");
        System.out.println();

        // 测试案例3: 单个行为
        System.out.println("=== 测试案例3: 单个行为 ===");
        List<String> userBehaviors3 = Arrays.asList("X");
        System.out.println("用户行为: " + userBehaviors3);
        try {
            System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors3));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("异常: IndexOutOfBoundsException - " + e.getMessage());
        }
        System.out.println("预期: IndexOutOfBoundsException");
        System.out.println();

        // 测试案例4: 长序列
        System.out.println("=== 测试案例4: 长序列 ===");
        List<String> userBehaviors4 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userBehaviors4.add("action_" + i);
        }
        System.out.println("用户行为长度: " + userBehaviors4.size());
        try {
            System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors4));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("异常: IndexOutOfBoundsException - " + e.getMessage());
        }
        System.out.println("预期: IndexOutOfBoundsException");
        System.out.println();

        // 测试案例5: 包含特殊字符
        System.out.println("=== 测试案例5: 包含特殊字符 ===");
        List<String> userBehaviors5 = Arrays.asList("login", "browse", "add_cart", "checkout");
        System.out.println("用户行为: " + userBehaviors5);
        try {
            System.out.println("结果: " + test.attainSameRouteMaxLength(userBehaviors5));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("异常: IndexOutOfBoundsException - " + e.getMessage());
        }
        System.out.println("预期: IndexOutOfBoundsException");
        System.out.println();

        System.out.println("=== 总结 ===");
        System.out.println("所有测试都会抛出 IndexOutOfBoundsException，因为：");
        System.out.println("1. riskPath 是空的 ArrayList");
        System.out.println("2. 代码尝试访问 riskPath.get(firstIndex)，但 firstIndex = 0，而 riskPath 大小为 0");
        System.out.println("3. 这会导致 IndexOutOfBoundsException: Index 0 out of bounds for length 0");
    }
}

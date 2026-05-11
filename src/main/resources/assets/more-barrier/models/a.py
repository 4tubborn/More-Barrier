import os
import json

# 配置路径：指向你存放生成的 json 文件的根目录
# 比如：'src/main/resources/assets/more-barrier'
target_dir = r'D:\FabricMods-DEV\more-barrier-1.21.11\src\main\resources\assets\more-barrier'

def batch_replace_json_values(directory):
    # 需要替换的特征字符
    search_str = "minecraft:block/acacia"
    # 目标值
    replace_str = "more-barrier:block/barrier"

    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith(".json"):
                file_path = os.path.join(root, file)
                
                with open(file_path, 'r', encoding='utf-8') as f:
                    try:
                        content = f.read()
                    except Exception as e:
                        print(f"读取失败 {file}: {e}")
                        continue

                # 检查是否包含目标字符串
                if search_str in content:
                    # 这里的逻辑是：只要包含那个路径前缀，不管后面带不带 _top, _bottom
                    # 比如 "minecraft:block/acacia_door_top" 这种也会被整个换掉
                    # 我们用正则表达式或者简单的逻辑来处理包含该字符串的完整“值”
                    
                    try:
                        data = json.loads(content)
                        
                        # 递归处理嵌套的 JSON 字典和列表
                        def process_node(node):
                            if isinstance(node, dict):
                                return {k: process_node(v) for k, v in node.items()}
                            elif isinstance(node, list):
                                return [process_node(i) for i in node]
                            elif isinstance(node, str):
                                # 核心逻辑：如果字符串包含特征路径，直接覆盖为 barrier
                                if search_str in node:
                                    return replace_str
                                return node
                            return node

                        new_data = process_node(data)

                        # 写回文件
                        with open(file_path, 'w', encoding='utf-8') as f:
                            json.dump(new_data, f, indent=2, ensure_ascii=False)
                        print(f"已修改: {file_path}")

                    except Exception as e:
                        print(f"解析 JSON 失败 {file}: {e}")

if __name__ == "__main__":
    if os.path.exists(target_dir):
        batch_replace_json_values(target_dir)
        print("\n所有匹配项已替换为 more-barrier:block/barrier")
    else:
        print("路径不存在，请检查 target_dir 配置")
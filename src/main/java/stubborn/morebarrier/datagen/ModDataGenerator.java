package stubborn.morebarrier.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // 关键：你必须手动把你的模型生成器类添加进去
        //pack.addProvider(ModModelGenerator::new);

        // 如果你有其他的生成器，它们应该也在这里，例如：
        // pack.addProvider(ModLootTableProvider::new);
        // pack.addProvider(ModLanguageProvider::new);
    }
}
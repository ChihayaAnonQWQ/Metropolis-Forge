# Métropolis — Minecraft 1.20.1 Forge 移植

Métropolis（[Minecraft Transit Railway](https://modrinth.com/mod/minecraft-transit-railway) 的现代铁道部件模组）
的 **Forge 1.20.1** 移植版。

- 上游项目：[DoveCotMC/Metropolis](https://github.com/DoveCotMC/Metropolis)
- 移植自上游分支 `1.20.1-fabric-mtr4`（`mod_version = 0.7.1-beta`）
- 本仓库：`ChihayaAnonQWQ/Metropolis-Forge`（上游的 fork），移植代码在 `1.20.1-forge` 分支
- 许可证：**MIT**（沿用上游）

> 完整的移植说明、逐项对照表与踩坑记录见 **[移植说明.md](移植说明.md)**。

---

## 环境要求

| 项目 | 版本 |
| --- | --- |
| Minecraft | 1.20.1 |
| Forge | 47.4.10（`mods.toml` 声明 `[47,)`） |
| JDK（编译） | **17** |
| MTR | **MTR 3.x，包名 `mtr`**（本移植对接的是 MTR 3 的公开字段式 API，不是 MTR 4 的 `org.mtr`） |

> ⚠️ 上游 0.7.x 官方是给 **MTR 4**（`org.mtr`）写的；本移植按实际运行环境改写为对接 **MTR 3**，
> 因此能与 MTR 3 生态（JCM / ANTE / YUM 等）共存。

## 构建

```bash
# 1. 放入 MTR 3 的 jar（不随仓库分发，MTR 为 All Rights Reserved）
mkdir -p libs
cp /path/to/MTR-forge-1.20.1-3.6.3.jar libs/

# 2. 用 JDK 17 构建
export JAVA_HOME=/path/to/jdk-17
./gradlew build
# 产物：build/libs/Metropolis-forge-1.20.1-0.7.1-beta-forge.jar
```

`libs/*.jar` 已在 `.gitignore` 中；`build.gradle` 用 `fileTree` 读取，文件名不必完全一致。

## 安装

1. 装好 Forge 1.20.1；
2. 把 `Metropolis-forge-1.20.1-0.7.1-beta-forge.jar` 和你的 **MTR 3** 放进 `mods/`；
3. 启动即可。`mods.toml` 里 MTR 是**可选依赖**，缺 MTR 时模组本体仍能加载，
   但「旧版内容」（闸机 / 售票机 / 安检门 / 半自动 PSD 等）不会注册。

## 与上游 Fabric 版的差异

- **平台门控制器**：修好红石判定（原版用 `Level#hasSignal` 判「是否通电」，语义不对，
  正常接线永远判不出），并**不再要求站台地面必须是 `platform_a`**——
  改为沿「门 / 栏杆这一排」遍历，任何地面都能用；ticker 也改为仅逻辑服务端。
- **网络**：Fabric 的任意通道改成单个 `SimpleChannel` 多路复用，语义与调用点保持原样。
- **配置界面**：不再经 ModMenu，改用 Forge 自带的「Mod 列表 → Config」。
- **数据生成（datagen）**：未移植（上游发布的 jar 同样不含 `data/`，行为一致）。
- **列车联动屏蔽门**：上游靠 MTR 4 的 `RenderVehicleHelper#canOpenDoors`；MTR 3 无此结构，
  本移植用 `MixinTrainDoorScan` 在 `mtr.data.Train#scanDoors` 上做了等价实现（含把门开度推给
  `IBlockMTRPlatformDoor` / 半高门）。**推荐做法**仍是用 MTR 的**时间表传感器**输出红石信号
  接到 `platform_door_controller_pillar`——既能到站开门，又能独立控制，且不依赖 Mixin 行为。
- 移除了 3 个仅对 MTR 4 有意义的 Mixin（`AccessorMTRMain`、`AccessorMTRInit`、
  `MixinRenderVehicleHelper`，最后一个已按 MTR 3 重写为上述 `MixinTrainDoorScan`）。

## 平台门用法

1. 铺站台（`platform_a` 或任意方块皆可），在边缘放 **`platform_door_half_height`** 或
   **`platform_door_half_height_glass`**（会滑动）；`platform_fence_half_height` 是固定栏杆，
   **永远不会动**（上游同样如此）。
2. 在门排旁边立 **`platform_door_controller_pillar`**（控制器立柱）。
3. 右键控制器立柱，或给它红石信号（拉杆 / 红石粉 / 中继器 / 红石块贴近**控制器**，不是贴门），
   同一排的门会一起开合。
4. 想「列车到站自动开门」：用 MTR 的**时间表传感器**输出红石接到控制器。

站台地面**不必**是 `platform_a`（本移植已放宽）。

## 许可与致谢

本项目沿用上游的 **MIT License**（见 [LICENSE](LICENSE)）。

上游作者与贡献者：Arrokoth233、Radekemia、HQIDK、ShirahaneExpress、Yomi_307、Dusbrio、
opera7133、RAO_dIE。

本移植为社区作品，与上游作者无隶属关系。Minecraft Transit Railway 版权归其作者
（Jonafaan Ho / jonafanho）所有，本项目不包含、也不分发 MTR 的任何代码或资源。

---

## English summary

Port of [Métropolis](https://github.com/DoveCotMC/Metropolis) (an addon for Minecraft Transit Railway)
to **Minecraft 1.20.1 / Forge 47.4.10**, targeting the **MTR 3 API (package `mtr`)** instead of MTR 4.

- Branch `1.20.1-forge` contains a self-contained ForgeGradle project.
- Requires JDK 17 to build; drop your own MTR 3 jar into `libs/` (not redistributed here).
- Fixes included: platform door controller redstone detection, controller no longer requires
  `platform_a` flooring, server-authoritative controller ticker, MTR 3 networking and client event
  rewrites, and a `Train#scanDoors` mixin for train-driven platform doors.
- Licensed MIT, same as upstream.

# PC Analyzer

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-brightgreen?logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql)
![Maven](https://img.shields.io/badge/Build-Maven-red?logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

**PC Analyzer** is a local Java application built on Spring Boot with a Thymeleaf web interface that automates the selection of optimal PC configurations. It collects current prices from the Ukrainian marketplace [Hotline](https://hotline.ua) and performance metrics from [UserBenchmark](https://userbenchmark.com), calculates cost per unit of performance (₴/FPS), and generates an Excel report with ranked builds sorted by efficiency.

---
## Current Status

The scraping pipeline is currently blocked by updated bot protection on Hotline.ua.
The collected dataset from the last successful run is available in the repository
and serves as the basis for the analytical part of the project.

Analytical work on the existing dataset is in progress — see the Excel report
and planned analysis sheet above.

---

## Table of Contents

- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Data Sources](#data-sources)
- [Compatibility Logic & Calculations](#compatibility-logic--calculations)
- [Output](#output)
- [Setup](#setup)
- [API & Web Interface](#api--web-interface)
- [Author](#author)

---

## Key Features

- **Multithreaded parsing** of 6 component categories from [Hotline](https://hotline.ua) via Jsoup (CPU, GPU, motherboards, RAM, SSD, PSU).
- **Benchmark parsing** from [UserBenchmark](https://userbenchmark.com) via Selenium WebDriver (Chrome) to bypass bot protection.
- **Conservative price calculation:** `avgPrice = Math.min((min+max)/2, min*1.15)`.
- **Automatic name normalization and synchronization** between Hotline and UserBenchmark (longest-to-shortest model matching).
- **Component compatibility checking:** Socket, Intel/AMD chipsets, DDR4/DDR5, PSU wattage.
- **Predicted FPS and price-per-frame calculation** (`priceForFps`).
- **Non-competitive build filtering** and `BEST_PRICE` marking.
- **Excel report generation** with automatic formatting, auto-filters, and color coding.
- **Web interface** for data management, log viewing, and report download.

---

## Tech Stack

| Category | Technologies |
|----------|--------------|
| **Backend** | Java 17, Spring Boot 3.4.1 (MVC, Data JPA, WebSocket), Flyway, MapStruct, Lombok |
| **Data Parsing** | Jsoup (multithreaded HTML), Selenium WebDriver (Chrome) |
| **Reporting** | Apache POI (Excel generation) |
| **Database** | MySQL |
| **Frontend** | Thymeleaf (SSR), HTML, WebSocket |
| **Infrastructure** | Maven, Docker, Checkstyle, JUnit |

---

## Data Sources

### Hotline — Ukrainian Prices

Six component categories are parsed via Jsoup (multithreaded, with retry on errors):

| Category | Data Collected |
|----------|----------------|
| **CPU** | Model, URL, prices, average price, socket, frequency, L3 cache, cores/threads, package type, offer count |
| **GPU** | Name, URL, manufacturer, memory type/size, bus, PCIe port, prices, offer count |
| **Motherboard** | Name, URL, socket, chipset, memory type, form factor, prices, offer count |
| **RAM** | Name, URL, DDR type, capacity, frequency (MHz), prices, offer count |
| **SSD** | Name, URL, type, capacity, read/write speed, prices, offer count |
| **PSU** | Name, URL, wattage, standard, MB/GPU connectors, prices, offer count |

Price string `"12 345 грн – 15 678 грн"` is processed conservatively:
```java
avgPrice = Math.min((min + max) / 2, min * 1.15);
```

### UserBenchmark — Performance Data

Parsing via Selenium WebDriver (Chrome) because the site blocks bots. After the page loads, HTML is analyzed with Jsoup.

**CPU:** visits each processor's detail page to retrieve `gamingScore`, `desktopScore`, `workstationScore`, core count, and thread count. Uses 3 CSS template fallbacks in case the site layout changes.

**GPU:** collects model, rating, average benchmark. `PowerRequirement` is entered manually through the web interface for new cards.

### Name Normalization

To match models between both sources, names are normalized:
- `"RTX 4070-Ti"` → `"RTX 4070 Ti"`
- `"Core i7-13700KF"` → `"Core i7-13700K"`

Synchronization runs from the longest model name to the shortest to avoid false matches.

---

## Compatibility Logic & Calculations

### Build Assembly

1. **Filtering:** selects components with >5 offers on Hotline (indicating availability) and existing benchmark data.

2. **Compatibility:**
   - **CPU ↔ MB:** socket match + chipset logic:
      - Intel **K**-series → **Z**-chipset
      - AMD **X**-series → **X**-chipset
      - i5/i7/i9 or Ryzen 5/7/9 (non-K/X) → **B**-chipset
      - Others → any chipset
   - **MB ↔ RAM:** matching memory type (DDR4/DDR5)
   - **GPU ↔ PSU:** cheapest PSU with wattage ≥ GPU power requirement

3. **Calculations:**
   ```
   totalPrice = CPU + cooling + case + MB + RAM + GPU + SSD + PSU
   predictionGpuFpsFhd = gamingScore * avgGpuBench / 100
   priceForFps = totalPrice / predictionGpuFpsFhd
   ```

4. **Pruning:** builds with lower FPS but higher price are removed.

5. **Marking:** best-value builds receive the `BEST_PRICE` label.

---

## Output

Generates `pc_configuration_yyyy-MM-dd HH-mm.xlsx`:

| Column | Description |
|--------|-------------|
| **Part Number** | Unique build identifier |
| **CPU** | Processor name + Hotline link |
| **Motherboard** | Motherboard name + link |
| **Memory** | RAM name + link |
| **GPU** | Graphics card name + link |
| **SSD** | SSD name + link |
| **Power Supplier** | PSU name + link |
| **Price** | Total build cost (₴) |
| **Prediction FPS** | Predicted FPS at Full HD |
| **Gaming Score** | CPU gaming benchmark |
| **Price per FPS** | Cost per frame (₴/frame) — key efficiency metric |
| **Marker** | `BEST_PRICE` for optimal builds |

The file has a frozen header row, auto-filter, and color coding: green headers, alternating rows, best builds highlighted in light blue.

Sample report: [`pc_configuration 2025-02-09 11-53.xlsx`](src/main/resources/files/pc_configuration%202025-02-09%2011-53.xlsx)

![Excel report screenshot](src/main/resources/files/xlsx.png)

### Analytical Capabilities of the Excel Report

- **Build efficiency comparison** — sorting by ₴/FPS shows which configuration delivers the most frames for the least money.
- **Component contribution analysis** — separate Gaming Score CPU and Avg Bench GPU columns help identify which component limits performance.
- **Budget planning** — filtering by the Price column finds builds within a given budget (e.g., up to ₴30,000).
- **Bottleneck detection** — analyzing the ratio of Gaming Score CPU to Avg Bench GPU reveals whether the CPU or GPU is the bottleneck.
- **Custom filtering** — the built-in Excel auto-filter allows selecting builds by any column: specific CPU, GPU, price range, or FPS.
- **Visual marking** — the `BEST_PRICE` label instantly highlights builds with the optimal price-to-performance ratio.

---

## Setup

### Option 1: Maven + Local MySQL

```bash
# 1. Clone the repository
git clone https://github.com/ArtemMakovskyy/pc_analyzer.git
cd pc_analyzer

# 2. Configure MySQL in application.properties

# 3. Run
mvn spring-boot:run
```

### Option 2: Docker

```bash
git clone https://github.com/ArtemMakovskyy/pc_analyzer.git
cd pc_analyzer

mvn clean package
docker-compose up -d

# Stop
docker-compose down
```

The application is available at: **http://localhost:8079/**

> **Note:** when deploying to the internet, replace `localhost:8079` with the public address in `src/main/resources/templates/operations.html`.

---

## API & Web Interface

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/gpus` | List of graphics cards |
| `POST` | `/saveGpus` | Update GPU power requirements |
| `GET` | `/description` | Description page |
| `POST` | `/operations/execute` | Execute operations (data update, build assembly) |

---

## Author

**Artem Makovskyy**
- Email: artem.makovskyi.jv@gmail.com
- [LinkedIn](https://www.linkedin.com/in/artem-makovskyi-557783304/)
- [GitHub](https://github.com/ArtemMakovskyy)

# FileSort

A rule-based file organization tool that automatically sorts and manages files across directories. Built with an extensible design so new rules can be added over time without changing the core system.

---

## Overview
FileSort scans directories (including nested folders) and organizes files based on configurable rules such as file type and extensions. The system is designed to evolve—new rules can be introduced as new file patterns and use cases come up.

---

## Features
- Recursive directory traversal  
- Rule-based file sorting system  
- Supports grouping by file type (images, documents, spreadsheets, etc.)  
- Extensible architecture for adding new rules  
- Safe handling of unmatched files (files remain in place if no rule applies)  

---

## Current Rules
- Images → `png`, `jpg`, `jpeg`  
- Spreadsheets → `csv`, `xlsx`  
- Documents → `pdf`  

---

## How It’s Evolving
The project is actively being expanded by adding new rules and improving flexibility. Planned improvements include:
- Filename-based classification (e.g., detecting "resume", "invoice")  
- Enhanced duplicate handling  
- More dynamic and configurable rule definitions  

---

## Example Rule
```java
new Rule(List.of("csv", "xlsx"), "Sheets");

# Importing 9000+ Medicines from CSV

To import your large CSV file into the app, we will use Room's `createFromAsset` feature. This is the most efficient way to handle 9000+ records.

## Step 1: Prepare your CSV
Ensure your CSV has the following columns (the order must match the `Medicine` entity):
- `id` (Integer)
- `brandName` (String)
- `genericName` (String)
- `brandedPrice` (Double)
- `genericPrice` (Double)
- `category` (String)
- `dosageForm` (String)
- `saltComposition` (String)

## Step 2: Convert CSV to SQLite
Since Room reads from a `.db` file in the assets, you need to convert your CSV to a SQLite database named `medicines.db`.

You can use a tool like [DB Browser for SQLite](https://sqlitebrowser.org/) to:
1. Create a new database.
2. Import your CSV into a table named `medicines`.
3. Ensure the column names match exactly.

## Step 3: Place the Database in Assets
Move your generated `medicines.db` to:
`app/src/main/assets/databases/medicines.db`

## Step 4: Update AppDatabase.kt
I have already prepared the code to switch from manual population to asset-based population. 

### Change required in `AppDatabase.kt`:
```kotlin
// In AppDatabase.getDatabase()
val instance = Room.databaseBuilder(
    context.applicationContext,
    AppDatabase::class.java,
    "medicine_database"
)
.createFromAsset("databases/medicines.db") // <--- Add this line
.build()
```

## Alternative: Bulk Insert (If you prefer code)
If you want to keep the CSV as a raw file and parse it on first run:
1. Place `medicines.csv` in `app/src/main/assets/`.
2. Use a library like `opencsv` or a simple Kotlin `bufferedReader` to parse and call `medicineDao.insertAll(list)`.

> [!TIP]
> For 9000+ records, the **Pre-populated Database (Step 3)** is highly recommended as it makes the app launch much faster on the first run.

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.flowapp.model.Transaction

class TransactionRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addTransaction(transaction: Transaction) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("type", transaction.type)
            put("detail", transaction.detail)
            put("value", transaction.value)
            put("date", transaction.date)
        }
        db.insert("transactions", null, values)
    }

    fun getAllTransactions(): List<Transaction> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.query("transactions", null, null, null, null, null, null)
        val transactions = mutableListOf<Transaction>()

        try {

            val idIndex = cursor.getColumnIndexOrThrow("id")
            val typeIndex = cursor.getColumnIndexOrThrow("type")
            val detailIndex = cursor.getColumnIndexOrThrow("detail")
            val valueIndex = cursor.getColumnIndexOrThrow("value")
            val dateIndex = cursor.getColumnIndexOrThrow("date")

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val type = cursor.getString(typeIndex)
                val detail = cursor.getString(detailIndex)
                val value = cursor.getDouble(valueIndex)
                val date = cursor.getString(dateIndex)
                transactions.add(Transaction(id, type, detail, value, date))
            }
        } finally {
            cursor.close()
        }

        return transactions
    }


    fun getBalance(): Double {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(value) FROM transactions WHERE type = 'Crédito'", null)
        val creditSum = cursor.use {
            if (it.moveToFirst()) it.getDouble(0) else 0.0
        }
        val debitSumCursor = db.rawQuery("SELECT SUM(value) FROM transactions WHERE type = 'Débito'", null)
        val debitSum = debitSumCursor.use {
            if (it.moveToFirst()) it.getDouble(0) else 0.0
        }
        return creditSum - debitSum
    }
}

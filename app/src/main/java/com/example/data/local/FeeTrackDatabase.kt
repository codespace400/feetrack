package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.FeeStructureEntity
import com.example.data.model.PaymentEntity
import com.example.data.model.StudentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [StudentEntity::class, PaymentEntity::class, FeeStructureEntity::class],
  version = 1,
  exportSchema = false
)
abstract class FeeTrackDatabase : RoomDatabase() {
  abstract fun studentDao(): StudentDao
  abstract fun paymentDao(): PaymentDao
  abstract fun feeStructureDao(): FeeStructureDao

  companion object {
    @Volatile
    private var INSTANCE: FeeTrackDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): FeeTrackDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          FeeTrackDatabase::class.java,
          "feetrack_database"
        )
        .addCallback(FeeTrackDatabaseCallback(scope))
        .build()
        INSTANCE = instance
        instance
      }
    }

    private class FeeTrackDatabaseCallback(
      private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateInitialData(database)
          }
        }
      }
    }

    suspend fun populateInitialData(database: FeeTrackDatabase) {
      val studentDao = database.studentDao()
      val paymentDao = database.paymentDao()
      val feeStructureDao = database.feeStructureDao()

      if (studentDao.getCount() > 0) return

      // Seed Fee Structures per class for Term 2
      val classes = listOf("Grade 7", "Grade 8", "Grade 9", "Grade 10", "Grade 11", "Grade 12")
      val structures = mutableListOf<FeeStructureEntity>()

      classes.forEach { cls ->
        val baseTuition = when (cls) {
          "Grade 7", "Grade 8" -> 1200.0
          "Grade 9", "Grade 10" -> 1450.0
          else -> 1700.0
        }
        structures.add(FeeStructureEntity(classLevel = cls, term = "Term 2", category = "Tuition Fee", amount = baseTuition))
        structures.add(FeeStructureEntity(classLevel = cls, term = "Term 2", category = "Books & Stationery", amount = 250.0))
        structures.add(FeeStructureEntity(classLevel = cls, term = "Term 2", category = "School Uniform & Kit", amount = 180.0))
        structures.add(FeeStructureEntity(classLevel = cls, term = "Term 2", category = "Science & ICT Lab", amount = 150.0))
        structures.add(FeeStructureEntity(classLevel = cls, term = "Term 2", category = "Sports & Activities", amount = 120.0))
      }
      feeStructureDao.insertFeeStructures(structures)

      // Seed Initial Students
      val students = listOf(
        StudentEntity(
          studentCode = "FT-2026-001",
          name = "Liam Alexander Vance",
          classLevel = "Grade 10",
          gender = "M",
          guardianName = "Marcus Vance",
          guardianPhone = "+1 (555) 234-8901",
          guardianEmail = "m.vance@workmail.com",
          totalFee = 2150.0,
          amountPaid = 2150.0,
          balance = 0.0,
          status = "PAID"
        ),
        StudentEntity(
          studentCode = "FT-2026-002",
          name = "Amara Chioma Eze",
          classLevel = "Grade 11",
          gender = "F",
          guardianName = "Dr. Ngozi Eze",
          guardianPhone = "+1 (555) 871-3420",
          guardianEmail = "dr.eze@clinicnet.org",
          totalFee = 2400.0,
          amountPaid = 1600.0,
          balance = 800.0,
          status = "PARTIAL"
        ),
        StudentEntity(
          studentCode = "FT-2026-003",
          name = "Ethan Benjamin Hayes",
          classLevel = "Grade 12",
          gender = "M",
          guardianName = "Eleanor Hayes",
          guardianPhone = "+1 (555) 492-1184",
          guardianEmail = "eleanor.hayes@apexlaw.com",
          totalFee = 2400.0,
          amountPaid = 0.0,
          balance = 2400.0,
          status = "OWING"
        ),
        StudentEntity(
          studentCode = "FT-2026-004",
          name = "Sophia Isabella Martinez",
          classLevel = "Grade 9",
          gender = "F",
          guardianName = "Carlos Martinez",
          guardianPhone = "+1 (555) 732-9012",
          guardianEmail = "carlos.m@buildcorp.com",
          totalFee = 2150.0,
          amountPaid = 2150.0,
          balance = 0.0,
          status = "PAID"
        ),
        StudentEntity(
          studentCode = "FT-2026-005",
          name = "Noah Tariq Al-Mansoor",
          classLevel = "Grade 8",
          gender = "M",
          guardianName = "Tariq Al-Mansoor",
          guardianPhone = "+1 (555) 604-5519",
          guardianEmail = "tariq@skylineintl.com",
          totalFee = 1900.0,
          amountPaid = 1100.0,
          balance = 800.0,
          status = "PARTIAL"
        ),
        StudentEntity(
          studentCode = "FT-2026-006",
          name = "Zoe Gabrielle Chen",
          classLevel = "Grade 7",
          gender = "F",
          guardianName = "David Chen",
          guardianPhone = "+1 (555) 318-7724",
          guardianEmail = "david.chen@synapse.io",
          totalFee = 1900.0,
          amountPaid = 1900.0,
          balance = 0.0,
          status = "PAID"
        ),
        StudentEntity(
          studentCode = "FT-2026-007",
          name = "Lucas Elijah Wright",
          classLevel = "Grade 12",
          gender = "M",
          guardianName = "Rachel Wright",
          guardianPhone = "+1 (555) 902-4411",
          guardianEmail = "rachel.wright@novamedia.org",
          totalFee = 2400.0,
          amountPaid = 1200.0,
          balance = 1200.0,
          status = "PARTIAL"
        ),
        StudentEntity(
          studentCode = "FT-2026-008",
          name = "Khadijah Fatima Bello",
          classLevel = "Grade 11",
          gender = "F",
          guardianName = "Alhaji Bello",
          guardianPhone = "+1 (555) 540-3329",
          guardianEmail = "bello.holdings@westtrade.ng",
          totalFee = 2400.0,
          amountPaid = 2400.0,
          balance = 0.0,
          status = "PAID"
        ),
        StudentEntity(
          studentCode = "FT-2026-009",
          name = "Mateo Gabriel Rivera",
          classLevel = "Grade 8",
          gender = "M",
          guardianName = "Elena Rivera",
          guardianPhone = "+1 (555) 789-2156",
          guardianEmail = "elena.rivera@medtech.com",
          totalFee = 1900.0,
          amountPaid = 0.0,
          balance = 1900.0,
          status = "OWING"
        ),
        StudentEntity(
          studentCode = "FT-2026-010",
          name = "Aria Lynn Jenkins",
          classLevel = "Grade 7",
          gender = "F",
          guardianName = "Captain Arthur Jenkins",
          guardianPhone = "+1 (555) 431-8877",
          guardianEmail = "a.jenkins@maritime.gov",
          totalFee = 1900.0,
          amountPaid = 1400.0,
          balance = 500.0,
          status = "PARTIAL"
        ),
        StudentEntity(
          studentCode = "FT-2026-011",
          name = "Jamal Kwame Osei",
          classLevel = "Grade 10",
          gender = "M",
          guardianName = "Kwame Osei Sr.",
          guardianPhone = "+1 (555) 672-9133",
          guardianEmail = "kwame.osei@panagro.com",
          totalFee = 2150.0,
          amountPaid = 0.0,
          balance = 2150.0,
          status = "OWING"
        ),
        StudentEntity(
          studentCode = "FT-2026-012",
          name = "Maya Juliette Dupont",
          classLevel = "Grade 9",
          gender = "F",
          guardianName = "Claude Dupont",
          guardianPhone = "+1 (555) 834-1290",
          guardianEmail = "c.dupont@voguearch.fr",
          totalFee = 2150.0,
          amountPaid = 2150.0,
          balance = 0.0,
          status = "PAID"
        ),
        StudentEntity(
          studentCode = "FT-2026-013",
          name = "Caleb Samuel Kim",
          classLevel = "Grade 11",
          gender = "M",
          guardianName = "Grace Kim",
          guardianPhone = "+1 (555) 298-6541",
          guardianEmail = "grace.kim@pacificbio.com",
          totalFee = 2400.0,
          amountPaid = 1800.0,
          balance = 600.0,
          status = "PARTIAL"
        ),
        StudentEntity(
          studentCode = "FT-2026-014",
          name = "Isla Grace Campbell",
          classLevel = "Grade 7",
          gender = "F",
          guardianName = "Andrew Campbell",
          guardianPhone = "+1 (555) 512-3487",
          guardianEmail = "andrew.c@highlandeng.co",
          totalFee = 1900.0,
          amountPaid = 1900.0,
          balance = 0.0,
          status = "PAID"
        ),
        StudentEntity(
          studentCode = "FT-2026-015",
          name = "Sebastian Tyler Ross",
          classLevel = "Grade 12",
          gender = "M",
          guardianName = "Patricia Ross",
          guardianPhone = "+1 (555) 980-4521",
          guardianEmail = "p.ross@consultancy.net",
          totalFee = 2400.0,
          amountPaid = 600.0,
          balance = 1800.0,
          status = "PARTIAL"
        ),
        StudentEntity(
          studentCode = "FT-2026-016",
          name = "Fatima Zahra Khan",
          classLevel = "Grade 8",
          gender = "F",
          guardianName = "Ibrahim Khan",
          guardianPhone = "+1 (555) 345-6712",
          guardianEmail = "i.khan@globallogistics.pk",
          totalFee = 1900.0,
          amountPaid = 1900.0,
          balance = 0.0,
          status = "PAID"
        )
      )

      studentDao.insertStudents(students)

      // Query back to get assigned IDs
      val insertedStudents = studentDao.getAllStudentsFlow()

      // Seed Initial Payment History Transactions
      val initialPayments = listOf(
        PaymentEntity(
          receiptNumber = "REC-2026-8041",
          studentId = 1,
          studentCode = "FT-2026-001",
          studentName = "Liam Alexander Vance",
          classLevel = "Grade 10",
          amount = 1450.0,
          paymentMethod = "Bank Transfer",
          paymentDate = "2026-10-18",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "Wire Ref: TXN-94021-BOA",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8042",
          studentId = 1,
          studentCode = "FT-2026-001",
          studentName = "Liam Alexander Vance",
          classLevel = "Grade 10",
          amount = 700.0,
          paymentMethod = "Card / POS",
          paymentDate = "2026-10-18",
          term = "Term 2",
          category = "Books & Stationery",
          referenceNote = "Terminal POS Auth #8491",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8043",
          studentId = 2,
          studentCode = "FT-2026-002",
          studentName = "Amara Chioma Eze",
          classLevel = "Grade 11",
          amount = 1600.0,
          paymentMethod = "Bank Transfer",
          paymentDate = "2026-10-19",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "Stanbic Transfer #90124",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8044",
          studentId = 4,
          studentCode = "FT-2026-004",
          studentName = "Sophia Isabella Martinez",
          classLevel = "Grade 9",
          amount = 2150.0,
          paymentMethod = "Bank Transfer",
          paymentDate = "2026-10-19",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "Direct Deposit Chase #44910",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8045",
          studentId = 5,
          studentCode = "FT-2026-005",
          studentName = "Noah Tariq Al-Mansoor",
          classLevel = "Grade 8",
          amount = 1100.0,
          paymentMethod = "Cash",
          paymentDate = "2026-10-20",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "Paid at Bursary Desk Cashier #02",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8046",
          studentId = 6,
          studentCode = "FT-2026-006",
          studentName = "Zoe Gabrielle Chen",
          classLevel = "Grade 7",
          amount = 1900.0,
          paymentMethod = "Card / POS",
          paymentDate = "2026-10-20",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "Visa Ending in 8831",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8047",
          studentId = 7,
          studentCode = "FT-2026-007",
          studentName = "Lucas Elijah Wright",
          classLevel = "Grade 12",
          amount = 1200.0,
          paymentMethod = "Cheque",
          paymentDate = "2026-10-21",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "Cheque #00491 Wells Fargo",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8048",
          studentId = 8,
          studentCode = "FT-2026-008",
          studentName = "Khadijah Fatima Bello",
          classLevel = "Grade 11",
          amount = 2400.0,
          paymentMethod = "Bank Transfer",
          paymentDate = "2026-10-21",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "Zenith NIP / FBN882190",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8049",
          studentId = 10,
          studentCode = "FT-2026-010",
          studentName = "Aria Lynn Jenkins",
          classLevel = "Grade 7",
          amount = 1400.0,
          paymentMethod = "Bank Transfer",
          paymentDate = "2026-10-22",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "Salary Deduction / Navy Credit",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8050",
          studentId = 12,
          studentCode = "FT-2026-012",
          studentName = "Maya Juliette Dupont",
          classLevel = "Grade 9",
          amount = 2150.0,
          paymentMethod = "Bank Transfer",
          paymentDate = "2026-10-22",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "International Wire HSBC #7819",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8051",
          studentId = 13,
          studentCode = "FT-2026-013",
          studentName = "Caleb Samuel Kim",
          classLevel = "Grade 11",
          amount = 1800.0,
          paymentMethod = "Card / POS",
          paymentDate = "2026-10-23",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "Mastercard ending in 1944",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8052",
          studentId = 14,
          studentCode = "FT-2026-014",
          studentName = "Isla Grace Campbell",
          classLevel = "Grade 7",
          amount = 1900.0,
          paymentMethod = "Bank Transfer",
          paymentDate = "2026-10-23",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "Online Banking Transfer",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8053",
          studentId = 15,
          studentCode = "FT-2026-015",
          studentName = "Sebastian Tyler Ross",
          classLevel = "Grade 12",
          amount = 600.0,
          paymentMethod = "Cash",
          paymentDate = "2026-10-23",
          term = "Term 2",
          category = "Books & Stationery",
          referenceNote = "Partial Cash Deposit",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        ),
        PaymentEntity(
          receiptNumber = "REC-2026-8054",
          studentId = 16,
          studentCode = "FT-2026-016",
          studentName = "Fatima Zahra Khan",
          classLevel = "Grade 8",
          amount = 1900.0,
          paymentMethod = "Bank Transfer",
          paymentDate = "2026-10-23",
          term = "Term 2",
          category = "Tuition Fee",
          referenceNote = "Corporate Bursary Sponsorship",
          recordedBy = "Sarah Jenkins (Chief Bursar)"
        )
      )

      paymentDao.insertPayments(initialPayments)
    }
  }
}

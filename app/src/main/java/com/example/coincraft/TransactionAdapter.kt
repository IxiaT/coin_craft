import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coincraft.R

class TransactionAdapter(private val itemList: List<TransactionItem>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionsViewHolder>() {

    private val imageResId = R.drawable.balance

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transactions_item, parent, false)
        return TransactionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        val item = itemList[position]
        holder.textViewTitle.text = item.title
        holder.textViewAmount.text = item.amount
        holder.imageView.setImageResource(imageResId)
    }

    override fun getItemCount(): Int = itemList.size

    class TransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewAmount: TextView = itemView.findViewById(R.id.textViewAmount)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewIcon)
    }
}

package vcmsa.projects.budgetingbestie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private final Context context;

    public interface OnEditClickListener {
        void onEditClick(Expense expense);
    }

    public ExpenseAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvAmount, tvCategory, tvDate;
        Button btnEdit;
        ImageView ivReceipt;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivReceipt = itemView.findViewById(R.id.ivReceipt);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.tvDescription.setText(expense.getDescription());
        holder.tvAmount.setText("Amount: R" + expense.getAmount());
        holder.tvCategory.setText("Category: " + expense.getCategory());
        holder.tvDate.setText("Date: " + expense.getDate());

        if (expense.getReceiptPhotoUri() != null && !expense.getReceiptPhotoUri().isEmpty()) {
            holder.ivReceipt.setVisibility(View.VISIBLE);
            holder.ivReceipt.setImageURI(Uri.parse(expense.getReceiptPhotoUri()));
        } else {
            holder.ivReceipt.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditExpense.class);
            intent.putExtra("expenseId", expense.getId()); // Ensure this is a String ID

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void updateData(List<Expense> newData) {
        this.expenseList = newData;
        notifyDataSetChanged();
    }
}

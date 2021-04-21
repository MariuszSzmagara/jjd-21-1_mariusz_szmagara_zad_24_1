package pl.javastart.homebudget.transaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.javastart.homebudget.transaction.data.TransactionsRepository;
import pl.javastart.homebudget.transaction.model.Transaction;
import pl.javastart.homebudget.transaction.model.Type;

import java.util.List;

@Controller
public class TransactionsController {

    private final TransactionsRepository transactionsRepository;

    public TransactionsController(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    @PostMapping("/addNewTransaction")
    public String addNewTransaction(Transaction transaction) {
        transactionsRepository.add(transaction);
        return "successAdded";
    }

    @GetMapping("/chooseTransactionToModifyOrDelete")
    public String chooseTransactionToModify(Model model) {
        List<Transaction> allTransactionsList = transactionsRepository.getAll();
        model.addAttribute("allTransactionsList", allTransactionsList);
        return "chooseTransactionToModifyOrDelete";
    }

    @GetMapping("/modifyTransactionById")
    public String editTransactionById(@RequestParam int id, Model model) {
        List<Transaction> allTransactionsList = transactionsRepository.getAll();
        for (Transaction transaction : allTransactionsList) {
            if (transaction.getId().equals(id)) {
                model.addAttribute("transaction", transaction);
                break;
            }
        }
        return "modifyTransactionForm";
    }

    @PostMapping("/updateTransactionById")
    public String updateTransactionById(Transaction transaction) {
        transactionsRepository.updateById(transaction);
        return "successAdded";
    }

    @GetMapping("/deleteTransactionById")
    public String deleteTransactionById(int id) {
        transactionsRepository.deleteById(id);
        return "successAdded";
    }

    @GetMapping("/chooseTransactionType")
    public String chooseTransactionType(Type transactionType, Model model) {
        model.addAttribute("transactionType", transactionType);
        return "chooseTransactionType";
    }

    @GetMapping("/transactionsListByType")
    public String getTransactionsListByType(@RequestParam Type type, Model model) {
        List<Transaction> transactionsListByType = transactionsRepository.searchByType(type);
        model.addAttribute("transactionsListByType", transactionsListByType);
        model.addAttribute("type", type);
        return "transactionsListByType";
    }
}

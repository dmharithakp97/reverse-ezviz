<?php

/**
 * RECONSTRUCTION — NOT the original source.
 *
 * app/Http/Controllers/EbillController.php  — method store()
 *
 * Reconstructed from the production Laravel debug stack trace at
 *   https://kandy.ebill.myrewards.lk/e-bill/subscribe/store   (HTTP 500)
 * captured in ../evidence/error_page_debug_leak.png
 *
 * The debug page exposed source lines 305-314 verbatim (shown as <<leaked>> below);
 * the surrounding code is inferred from Laravel conventions and the generated SQL:
 *   select * from `CRM_Customer` where (`merchant_id` = 200200 and `contact_no` = 772152986) limit 1
 * The crash: `merchant_id` is not a column on CRM_Customer  (SQLSTATE[42S22] 1054).
 *
 * Framework: Laravel 8.x  (fruitcake/laravel-cors + fideloper/proxy present in the trace)
 * Deploy root: /var/www/kandy.myrewards.lk/
 */

namespace App\Http\Controllers;

use App\Models\CrmCustomer;                 // -> table `CRM_Customer`
use Illuminate\Http\Request;

class EbillController extends Controller
{
    /**
     * POST /e-bill/subscribe/store
     * Confirm/create an e-bill subscription for a merchant (store), keyed by mobile number.
     */
    public function store(Request $request)
    {
        // --- inferred: validate + resolve tenant ------------------------------
        $merchantId = /* resolved from subdomain/config, e.g. */ 200200;      // "kandy" tenant
        $contactNo  = $request->input('contact_no');                          // e.g. 772152986
        $code       = /* random OTP, e.g. */ random_int(100000, 999999);

        // --- inferred: create/update the subscription+OTP row -----------------
        //     (the array whose last element is the <<leaked>> line 305)
        SomeSubscriptionModel::updateOrCreate(
            [ /* merchant + contact scope */ ],
            [
                // ... other fields ...
/* 305 */       'verification_code' => encrypt($code),   // <<leaked>>  reversible! see F-4
/* 306 */   ]);
/* 307 */   // }  (end of the create/update block)
/* 308 */
/* 309 */   # if crm customer update table                                    // <<leaked>> comment
/* 310 */   $crm_customer = CrmCustomer::where('merchant_id', $merchantId)     // <<leaked>>  ← CRASH
                              ->where('contact_no', $contactNo)                //   'merchant_id' does
                              ->first();                                       //   not exist on table
/* 311 */
/* 312 */   if ($crm_customer) {                                              // <<leaked>>
/* 313 */       $crm_customer->update(["has_subscribed" => /* 1 / true */]);  // <<leaked>> (truncated
/* 314 */   }                                                                 //   in the screenshot)

        // --- inferred: respond / redirect to the OTP verify step --------------
        return redirect()
            ->route('ebill.subscribe.verify')     // enter OTP -> decrypt(verification_code) & compare
            ->with('status', 'A verification code has been sent.');
    }
}

/*
 * DATABASE (as leaked)
 * --------------------
 * table `CRM_Customer`
 *   contact_no          int-ish     bound as integer 772152986 in the query
 *   verification_code   blob/text   Laravel encrypt() ciphertext (reversible)  -- F-4
 *   has_subscribed      bool/int    subscription flag set by store()
 *   merchant_id         <MISSING>   referenced by code, absent from schema     -- F-5 (the crash)
 *
 * Likely real scoping column (drifted from code): store_id / vec_chart_id / a relation table.
 */

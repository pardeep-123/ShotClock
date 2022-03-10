package com.app.shotclock.retrofit

import com.app.shotclock.constants.ApiConstants
import com.app.shotclock.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import retrofit2.http.Body

interface ApiService {

    // user login
    @POST(ApiConstants.USER_LOGIN)
    suspend fun loginUser(@Body body: LoginRequestModel): LoginResponseModel

    // forgot password
    @POST(ApiConstants.FORGOT_PASSWORD)
    suspend fun forgotPassword(@Body data: ForgotPasswordRequest): BaseResponseModel

    // user signup
    @Multipart
    @POST(ApiConstants.USER_SIGNUP)
    suspend fun userSignUp(@PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>): SignUpResponseModel

    @POST(ApiConstants.SOCIAL_LOGIN)
    suspend fun socialLogin(@Body data: SocialLoginRequestModel):BaseResponseModel

    //change password
    @POST(ApiConstants.CHANGE_PASSWORD)
    suspend fun changePassword(@Body data: ChangePassRequestModel): BaseResponseModel

    // terms and conditions
    @GET(ApiConstants.TERMS_AND_CONDITIONS)
    suspend fun termsConditions(): TermsAndConditionResponse

    // copy right notice
    @GET(ApiConstants.COPY_RIGHT_NOTICE)
    suspend fun copyRightNotice(): CopyRightNoticeResponse

    // safe dating policy
    @GET(ApiConstants.SAFE_DATING_POLICY)
    suspend fun safeDatingPolicy(): SafeDatingPolicyResponse

    // privacy policy
    @GET(ApiConstants.PRIVACY_POLICY)
    suspend fun privacyPolicy(): PrivacyPolicyResponse

    @GET(ApiConstants.COOKIE_POLICY)
    suspend fun cookiePolicy(): CookiePolicyResponse

    @GET(ApiConstants.LOGOUT)
    suspend fun userLogout(): BaseResponseModel

    @GET(ApiConstants.USER_PROFILE)
    suspend fun userProfile(): ProfileViewModel

    @Multipart
    @POST(ApiConstants.FILE_UPLOAD)
    suspend fun fileUpload(@Part image : ArrayList<MultipartBody.Part>, @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>):FileUploadResponse

    @POST(ApiConstants.COMPLETE_PROFILE)
    suspend fun completeProfile(@Body data:CompleteProfileRequestModel):CompleteProfileResponse

    @POST(ApiConstants.EDIT_PROFILE)
    suspend fun editProfile(@Body data: EditProfileRequestModel): EditProfileResponse

    @FormUrlEncoded
    @POST(ApiConstants.HOME_API)
    suspend fun homeApi(@Field("latitude") latitude: String ,@Field("longitude") longitude: String):HomeResponseModel

    @POST(ApiConstants.FILTER)
    suspend fun filterApi(@Body data: FilterRequestModel): HomeResponseModel

    @GET(ApiConstants.SUBSCRIPTION_PLANS)
    suspend fun subscriptionPlans(): SubscriptionPlansResponse

    @POST(ApiConstants.SELECTION_DONE)
    suspend fun selectionDone(@Body data: SelectionDoneRequestModel): SelectionDoneResponse

    @GET(ApiConstants.REQUEST_LIST)
    suspend fun requestList(): RequestListResponseModel

    @GET(ApiConstants.REQUEST_ALL)
    suspend fun allRequestList():AllRequestResponseModel

    @GET(ApiConstants.GET_NOTIFICATION)
    suspend fun getNotifications(): GetNotificationResponse

    @GET(ApiConstants.RECEIVED_LIST)
    suspend fun receivedList(): ReceivedListResponseModel

    @GET(ApiConstants.ICE_BREAKER_QUESTIONS)
    suspend fun iceBreakerQuestions(): IceBreakerQuestionResponse

    @POST(ApiConstants.CANCEL_REQUEST_ADMIN)
    suspend fun cancelRequestAdmin(@Body data: CancelRequestAdminRequest): BaseResponseModel

    @POST(ApiConstants.ACCEPT_DECLINE_REQUEST)
    suspend fun acceptDeclineRequest(@Body data: AcceptDeclineRequestModel): BaseResponseModel

}
